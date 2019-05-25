package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.resources.R;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * STATUS
 * LAST NOTIFICATION IF ANY
 * REQUEST / IDLE
 *
 * @author Abbo Giulio A.
 */
public class CLI implements InteractionInterface {
    private static final String SPACE = " ";
    private static final String CHOICE_INDEX = ") ";
    private final String lineSeparator;
    private final Object inputMon;
    private final Object currentsMon;

    private PrintStream out;
    private MatchState model;
    private StringBuilder currentRequest;
    private StringBuilder currentNotification;
    private boolean waitingForInput;
    private boolean handlingQuestion;
    private String input;

    public CLI(PrintStream out, InputStream in, MatchState model) {
        this.out = out;
        this.model = model;
        lineSeparator = System.lineSeparator();
        inputMon = new Object();
        currentsMon = new Object();
        currentRequest = new StringBuilder();
        currentNotification = new StringBuilder();
        waitingForInput = false;
        handlingQuestion = false;
        input = null;

        model.subscribe(this);
        new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while (true) {
                try {
                    newInput(reader.readLine());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }).start();
    }

    private void newInput(String input) {
        synchronized (inputMon) {
            if (waitingForInput) {
                this.input = input;
                waitingForInput = false;
                inputMon.notifyAll();
            }
        }
    }

    private void refresh() {
        synchronized (currentsMon) {
            //TODO: print status, notification, request, ?waiting
        }
    }


    @Override
    public void notifyUpdatedState() {
        refresh();
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
    public void drawState(MatchState state) {
        refresh();
    }

    @Override
    public String askName() {
        return handleQuestion("RequestName", null);
    }

    @Override
    public void sendNotification(String notificationKey) {
        handleNotification(notificationKey);
    }

    private String handleQuestion(String requestKey, List<List<String>> optionKeys) {
        synchronized (inputMon) {
            while (handlingQuestion) {
                try {
                    inputMon.wait();
                } catch (InterruptedException forwarded) {
                    Thread.currentThread().interrupt();
                }
            }

            synchronized (currentsMon) {
                currentRequest = (optionKeys == null) ?
                        translateQuestion(requestKey) :
                        translateQuestion(requestKey, optionKeys);
            }
            refresh();

            handlingQuestion = true;
            waitingForInput = true;
            try {
                while (waitingForInput)
                    inputMon.wait();
            } catch (InterruptedException forwarded) {
                Thread.currentThread().interrupt();
            }

            handlingQuestion = false;
            inputMon.notifyAll();
            return input;
        }
    }

    private void handleNotification(String notificationKey) {
        synchronized (currentsMon) {
            currentNotification = translateNotification(notificationKey);
        }
        refresh();
    }

    private StringBuilder translateNotification(String notificationKey) {
        return new StringBuilder()
                .append(R.string("notificationLabel"))
                .append(SPACE)
                .append(R.string(notificationKey))
                .append(lineSeparator);
    }

    private StringBuilder translateQuestion(String requestKey, List<List<String>> optionKeys) {
        StringBuilder builder = new StringBuilder();
        builder.append(R.string("choiceRequestLabel"))
                .append(lineSeparator)
                .append(R.string(requestKey));
        for (int i = 0; i < optionKeys.size(); i++) {
            Iterator<String> iterator = optionKeys.get(i).iterator();
            builder.append(i)
                    .append(CHOICE_INDEX)
                    .append(R.string(iterator.next()));
            while (iterator.hasNext())
                builder.append(R.string("choiceSeparator"))
                        .append(SPACE)
                        .append(R.string(iterator.next()));
            builder.append(lineSeparator);
        }
        builder.append(R.string("inputRequestLabel"));
        return builder;
    }

    private StringBuilder translateQuestion(String requestKey) {
        return new StringBuilder()
                .append(R.string(requestKey))
                .append(lineSeparator)
                .append(R.string("inputRequestLabel"));
    }
}
