package it.polimi.ingsw.client.interaction.GUI;


import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.interaction.InteractionInterface;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.List;

public class SyncGUI implements InteractionInterface {

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
        model.subscribe(this);
        GUI.setModel(model);
    }

    @Override
    public void notifyUpdatedState() {
        GUI.notifyUpdatedState();

    }

    @Override
    public int chooseEffectSequence(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.chooseEffectSequence(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseSpawn(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.chooseSpawn(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        return indexButtonPressed;
    }

    @Override
    public int choosePowerup(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.choosePowerup(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseDestination(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.chooseDestination(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseWeapon(List<List<String>> optionKeys) {
        GUI.chooseWeapon(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseWeaponToBuy(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        GUI.chooseWeaponToBuy(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseWeaponToDiscard(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.chooseWeaponToDiscard(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseWeaponToReload(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.chooseWeaponToReload(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
                System.out.println(GUI.getAnswer() + "");
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseAction(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.chooseAction(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int choosePowerupForPaying(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.choosePowerupForPaying(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseUseTagBack(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.chooseUseTagBack(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
    }

    @Override
    public int chooseTarget(List<List<String>> optionKeys) {
        GUI.setAnswerGiven(false);
        GUI.chooseTarget(optionKeys);
        while (!GUI.isAnswerGiven()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        int indexButtonPressed = GUI.getAnswer();
        GUI.setAnswerGiven(false);
        GUI.setWaitForTurn();
        return indexButtonPressed;
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

    public void updateTimer(int seconds) {
        GUI.updateTimer(seconds);
    }

}
