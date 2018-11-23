package net.readybid.entities.chain;

import net.readybid.entities.core.EntityImpl;
import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 2/15/2017.
 */
public class HotelChainImpl extends EntityImpl implements HotelChain {

    private String code;
    private HotelChainType subtype;
    private HotelChain masterChain;

    public HotelChainImpl(){
        setType(EntityType.CHAIN);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSubtype(HotelChainType subtype) {
        this.subtype = subtype;
    }

    public void setMasterChain(HotelChain masterChain) {
        this.masterChain = masterChain;
    }

    public String getCode() {
        return code;
    }

    public HotelChainType getSubtype() {
        return subtype;
    }

    public HotelChain getMasterChain() {
        return masterChain;
    }

    @Override
    public String getMasterChainName() {
        return null == masterChain ? null : masterChain.getName();
    }

    @Override
    public boolean isMasterChain() {
        return HotelChainType.MASTER.equals(subtype);
    }

    @Override
    public String getMasterChainId() {
        return null == masterChain ? null : String.valueOf(masterChain.getId());
    }
}
