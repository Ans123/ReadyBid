package net.readybid.api.entity;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.interactors.core.entity.GetEntityResponse;
import net.readybid.app.use_cases.core.entity.ReadEntity;
import net.readybid.app.use_cases.core.entity.UpdateEntity;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import net.readybid.web.RbViewModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class EntityController {

    private final UpdateEntity updateEntity;
    private final ReadEntity readEntity;

    @Autowired
    public EntityController(
            UpdateEntity updateEntity,
            ReadEntity readEntity
    ) {
        this.updateEntity = updateEntity;
        this.readEntity = readEntity;
    }

    @RbResponseView
    @GetMapping(value = "/entities/{type}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel getEntity(
            @PathVariable(value = "type") String type,
            @PathVariable(value = "id") String entityId
    ){
        final GetEntityResponse entity = readEntity.get(EntityType.valueOf(type), entityId);
        if(entity == null) throw new NotFoundException();
        return new RbViewModel() {
            @Override
            public Object getData() {
                return entity;
            }
        };
    }

    @RbResponseView
    @PutMapping(value = "/entities/{type}/{id}/details")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setEntityBasicInformation(
            @RequestBody @Valid UpdateBasicInformationRequest request,
            @PathVariable(value = "type") String type,
            @PathVariable(value = "id") String entityId
    ){
        updateEntity.setBasicInformation(EntityType.valueOf(type), entityId, request.toEntityBasicInformationUpdateRequest());
        return RbViewModels.ACTION_SUCCESS;
    }

    @RbResponseView
    @PostMapping(value = "/entities/{type}/{id}/logo")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setEntityLogo(
            @RequestPart("file") MultipartFile file,
            @PathVariable(value = "type") String type,
            @PathVariable(value = "id") String entityId
    ){
        final String logoUrl = updateEntity.setLogo(EntityType.valueOf(type), entityId, file);
        return new RbViewModel() {
            @Override
            public Object getData() {
                return logoUrl;
            }
        };
    }

    @RbResponseView
    @PostMapping(value = "/entities/{type}/{id}/image")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setEntityImage(
            @RequestPart("file") MultipartFile file,
            @PathVariable(value = "type") String type,
            @PathVariable(value = "id") String entityId
    ){
        final String imageUrl = updateEntity.setImage(EntityType.valueOf(type), entityId, file);
        return new RbViewModel() {
            @Override
            public Object getData() {
                return imageUrl;
            }
        };
    }
}
