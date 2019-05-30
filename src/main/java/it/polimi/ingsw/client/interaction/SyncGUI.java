package it.polimi.ingsw.client.interaction;


import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import javafx.application.Application;
import javafx.application.Platform;

import java.util.List;

public class SyncGUI implements InteractionInterface{

    private GUI gui;
    private ClientController controller;
    private MatchState model;

    /*
     * Creates and runs the GUI
     */
    public SyncGUI() {
    }

    public void setController(ClientController controller) {
        this.controller = controller;
        this.gui = new GUI();
        this.gui.setController(controller);
        this.gui.setSynchGUI(this);
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(GUI.class);
            }
        }.start();
    }

    /**
     * Setting model
     *
     * @param model
     */
    @Override
    public void setModel(MatchState model) {
        this.model = model;
        gui.setModel(model);
    }

    @Override
    public void notifyUpdatedState() {
        gui.notifyUpdatedState();

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
        return 0;
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
        return 0;
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
        String name = gui.askName();
        while (name == "ERROR") {
            name = gui.askName();
        }

        return name;
    }

    @Override
    public void sendNotification(String notificationKey) {
        Platform.runLater(() -> gui.sendNotification(notificationKey));
    }

    @Override
    public void drawState() {

    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
