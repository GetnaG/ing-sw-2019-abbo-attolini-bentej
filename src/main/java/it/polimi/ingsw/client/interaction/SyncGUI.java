package it.polimi.ingsw.client.interaction;


import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import javafx.application.Application;
import javafx.application.Platform;

import java.util.List;

public class SyncGUI implements InteractionInterface{

    private ClientController controller;
    private MatchState model;

    /*
     * Creates and runs the GUI
     */
    public SyncGUI() {
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(GUI.class);
            }
        }.start();
    }

    public void setController(ClientController controller) {
        this.controller = controller;

        GUI.setController(controller);
        GUI.setSynchGUI(this);

    }

    /**
     * Setting model
     *
     * @param model
     */
    @Override
    public void setModel(MatchState model) {
        this.model = model;
        GUI.setModel(model);
    }

    @Override
    public void notifyUpdatedState() {
        GUI.notifyUpdatedState();

    }

    @Override
    public int chooseEffectSequence(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseSpawn(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int choosePowerup(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseDestination(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseWeapon(List<List<String>> optionKeys) {
        GUI.chooseWeapon(optionKeys);
        while (true) {
            if (GUI.isAnswerGiven())
                break;
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        return indexButtonPressed;
    }

    @Override
    public int chooseWeaponToBuy(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseWeaponToDiscard(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseWeaponToReload(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseAction(List<List<String>> optionKeys) {
        GUI.chooseAction(optionKeys);
        while (true) {
            if (GUI.isAnswerGiven())
                break;
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        return indexButtonPressed;
    }

    @Override
    public int choosePowerupForPaying(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseUseTagBack(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public int chooseTarget(List<List<String>> optionKeys) {
        return 0;
    }

    @Override
    public String askName() {
        String name = GUI.askName();
        while (name == "ERROR") {
            name = GUI.askName();
        }

        return name;
    }

    @Override
    public void sendNotification(String notificationKey) {
        Platform.runLater(() -> GUI.sendNotification(notificationKey));
    }

    @Override
    public void drawState() {

    }

    @Override
    public void setGUI(GUI gui) {
    }
}
