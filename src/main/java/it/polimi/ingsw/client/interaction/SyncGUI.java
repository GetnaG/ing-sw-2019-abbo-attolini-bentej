package it.polimi.ingsw.client.interaction;


import it.polimi.ingsw.client.clientlogic.ClientController;
import javafx.application.Application;

import java.util.List;

public class SyncGUI implements InteractionInterface{

    private GUI gui;
    private ClientController controller;
    /**
     * Creates and runs the GUI
     */
    public SyncGUI() {
    }

    public void setController(ClientController controller) {
        this.controller = controller;
        this.gui = new GUI(controller);
        Application.launch(GUI.class);
    }

    @Override
    public void notifyUpdatedState() {

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
        return null;
    }

    @Override
    public void sendNotification(String notificationKey) {

    }

    @Override
    public void drawState() {

    }
}
