package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.communication.MessageType;
import it.polimi.ingsw.communication.Notification;
import it.polimi.ingsw.communication.ProtocolMessage;
import it.polimi.ingsw.communication.Update;

import java.util.Arrays;

/**
 * Represents the Controller of the client. It elaborates the received Notifications, Updates and Commands .
 */
public class ClientController {

    private MatchState matchState;


    public void handleNotifications(Notification[] notifications) {


    }

    public void handleUpdates(Update[] updates) {
        Arrays.asList(updates).forEach(matchState::handleUpdate);

    }

    public String handleQuestion(ProtocolMessage message) {
        switch (message.getCommand()) {
            case NOTIFICATION:
                handleNotifications(message.getNotifications());
            case UPDATE:
                handleUpdates(message.getUpdates());
            case EFFECTS_SEQUENCE:
                // TODO: handle case
            case SPAWN:
                // TODO: handle case
            case POWERUP:
                // TODO: handle case
            case DESTINATION:
                // TODO: handle case
            case WEAPON:
                // TODO: handle case
            case WEAPON_TO_BUY:
                // TODO: handle case
            case WEAPON_TO_DISCARD:
                // TODO: handle case
            case WEAPON_TO_RELOAD:
                // TODO: handle case
            case ACTION:
                // TODO: handle case
            case POWERUP_FOR_PAYING:
                // TODO: handle case
            case USE_TAGBACK:
                // TODO: handle case
            case TARGET:
                // TODO: handle case
            default:
                // nothing


        }
        return null;
    }

}
