package net.readybid.auth.invitation;

/**
 * Created by DejanK on 5/18/2017.
 *
 */
public interface InvitationRepository {
    void create(Invitation invitation);

    Invitation getActiveInvitationById(String id);

    void cleanup();
}
