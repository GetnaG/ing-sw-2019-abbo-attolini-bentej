package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.resources.R;

import java.io.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.List;

/**
 * Command line interface, handles the communication with the user via text.
 * This will show the user the status of the match, the last notification,
 * the current request if there is one and whether this is waiting for input.
 * <p>
 * The match status is read from the model; this is also subscribed to the
 * model, so that it can be notified when changes occur.
 *
 * @author Abbo Giulio A.
 * @see PrintStream
 * @see InputStream
 */
public class CLI implements InteractionInterface {
    /**
     * A white space notation.
     */
    private static final String SPACE = " ";
    /**
     * This is placed between the choice index and its description when
     * multiple choices are available.
     */
    private static final String CHOICE_INDEX = ") ";
    /**
     * The line separator for this OS.
     */
    private final String lineSeparator;
    /**
     * A monitor for {@linkplain #input} and {@linkplain #waitingForInput}.
     */
    private final Object inputMon;
    /**
     * A monitor fo {@linkplain #currentNotification} and
     * {@linkplain #currentRequest}.
     */
    private final Object currentsMon;
    /**
     * The stream where text will be displayed.
     */
    private PrintStream out;
    /**
     * The class that handles the model; holds the status of the match.
     */
    private MatchState model;
    /**
     * The current request to be displayed.
     */
    private StringBuilder currentRequest;
    /**
     * The current notification to be displayed.
     */
    private StringBuilder currentNotification;
    /**
     * Whether this is waiting for an input from the user.
     */
    private boolean waitingForInput;
    /**
     * Whether this is currently handling a question.
     * Only one question can be handled at the time.
     */
    private boolean handlingQuestion;
    /**
     * Holds the last user input if one was expected.
     */
    private String input;
    /**
     * The controller, used only in the initial setup.
     */
    private ClientController controller;

    /**
     * Creates a CLI interface that uses the provided input and output, and
     * retrieves data from the provided model.
     * <p>
     * Starts a thread that listens for the user input, and calls
     * {@linkplain #newInput(String)} when detects one.
     *
     * @param out where the text will be displayed
     * @param in  the input from the user
     */
    public CLI(PrintStream out, InputStream in) {
        this.out = out;
        lineSeparator = System.lineSeparator();
        inputMon = new Object();
        currentsMon = new Object();
        currentRequest = new StringBuilder();
        currentNotification = new StringBuilder();
        waitingForInput = false;
        handlingQuestion = false;
        input = null;
        refresh();

        /*A new thread that listens for the input and calls newInput()*/
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

    private void askConnection() {
        if (controller != null) {
            String connection = handleQuestion("chooseConnection", null);
            if (connection.equals("socket")) {
                try {
                    controller.setSocket(
                            handleQuestion("chooseIP", null),
                            Integer.parseInt(handleQuestion("choosePort", null))
                    );
                } catch (IOException e) {
                    handleNotification("GenericError");
                }
            } else if (connection.equals("rmi")) {
                try {
                    UnicastRemoteObject.exportObject(controller, 0);
                    controller.setRmi(
                            handleQuestion("chooseIP", null));
                } catch (IOException e) {

                    e.printStackTrace();
                    handleNotification("GenericError");
                }
            } else {
                handleNotification("GenericError");
            }
        }
    }

    /**
     * Sets the model for this view.
     * This also subscribes this class to the provided model.
     *
     * @param model the class that will keep the match status
     */
    public void setModel(MatchState model) {
        this.model = model;

        /*Subscribing this to the model*/
        model.subscribe(this);
    }

    /**
     * This method is called by another thread when new input is available.
     *
     * @param input the new input
     */
    private void newInput(String input) {
        if (input == null)
            return;
        synchronized (inputMon) {
            if (waitingForInput) {
                this.input = input;
                waitingForInput = false;
                inputMon.notifyAll();
            }
        }
    }

    /**
     * Refreshes what the user can see.
     * This takes data from {@linkplain #model},
     * {@linkplain #currentNotification} and {@linkplain #currentRequest}.
     * If this object is waiting for input, this will display an indication
     * for it.
     */
    private void refresh() {
        out.println(translateStatus());
        synchronized (currentsMon) {
            out.println(currentNotification);
            out.println(currentRequest);
        }
        synchronized (inputMon) {
            if (waitingForInput)
                out.println(R.string("requestInputIndicator"));
        }
    }

    @Override
    public void notifyUpdatedState() {
        refresh();
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseEffectSequence(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askEffect", optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseSpawn(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askSpawn", optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int choosePowerup(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askPowerup", optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseDestination(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askSquare", optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseWeapon(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askWeapon",
                    optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseWeaponToBuy(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askWeaponBuy", optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseWeaponToDiscard(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askWeaponDiscard",
                    optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseWeaponToReload(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askWeaponReload",
                    optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseAction(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askAction", optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int choosePowerupForPaying(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askPowerupPay",
                    optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseUseTagBack(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askTagback", optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the index of the user choice or -1 if it was not an integer
     */
    @Override
    public int chooseTarget(List<List<String>> optionKeys) {
        try {
            return Integer.parseInt(handleQuestion("askTarget", optionKeys));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void drawState() {
        refresh();
    }

    @Override
    public String askName() {
        return handleQuestion("askName", null);
    }

    @Override
    public void sendNotification(String notificationKey) {
        handleNotification(notificationKey);
    }

    /**
     * Aks a question and returns the answer.
     * If there are options, this will return the index of the choice;
     * otherwise will return the text inserted.
     * <p>
     * This class can handle only one question at the time.
     *
     * @param requestKey the resource key for the question
     * @param optionKeys the options, containing the resource keys for the
     *                   sequence
     * @return the user input
     */
    private String handleQuestion(String requestKey, List<List<String>> optionKeys) {

        /*Only one question can be handled at the time*/
        synchronized (inputMon) {
            while (handlingQuestion) {
                try {
                    inputMon.wait();
                } catch (InterruptedException forwarded) {
                    Thread.currentThread().interrupt();
                }
            }

            /*Updating the current request*/
            synchronized (currentsMon) {
                currentRequest = (optionKeys == null) ?
                        translateQuestion(requestKey) :
                        translateQuestion(requestKey, optionKeys);
            }
            refresh();

            /*Starting to listen for input*/
            handlingQuestion = true;
            waitingForInput = true;
            try {
                while (waitingForInput)
                    inputMon.wait();
            } catch (InterruptedException forwarded) {
                Thread.currentThread().interrupt();
            }

            /*Obtained input: removing the request*/
            synchronized (currentsMon) {
                currentRequest = new StringBuilder();
            }
            refresh();

            /*Returning the input*/
            handlingQuestion = false;
            inputMon.notifyAll();
            return input;
        }
    }

    /**
     * Shows a notification; this notification will be wisible until a new
     * notification arrives.
     *
     * @param notificationKey the resource key for the notification text
     */
    private void handleNotification(String notificationKey) {
        synchronized (currentsMon) {
            currentNotification = translateNotification(notificationKey);
        }
        refresh();
    }

    /**
     * Takes the status from the model and puts it into a string in the right
     * lenguage.
     *
     * @return a string containing the status to be displayed
     */
    private StringBuilder translateStatus() {
        //TODO translate status
        return new StringBuilder().append("PUT STATUS HERE").append(lineSeparator);
    }

    /**
     * Returns a string containing the localized text for the notification.
     *
     * @param notificationKey the resource key for retrieving the notification
     * @return a string containing the localized text for the notification
     */
    private StringBuilder translateNotification(String notificationKey) {
        return new StringBuilder()
                .append(R.string("notificationHeader"))
                .append(SPACE)
                .append(R.string(notificationKey))
                .append(lineSeparator);
    }

    /**
     * Returns a string containing the localized text for the request with
     * options.
     *
     * @param requestKey the resource key for the request
     * @param optionKeys the options, containing the resource keys for the
     *                   sequence
     * @return a string containing the localized text for the request
     */
    private StringBuilder translateQuestion(String requestKey, List<List<String>> optionKeys) {
        StringBuilder builder = new StringBuilder();
        builder.append(R.string("requestChoiceHeader"))
                .append(lineSeparator)
                .append(R.string(requestKey))
                .append(lineSeparator);

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
        return builder;
    }

    /**
     * Returns a string containing the localized text for the request without
     * options.
     *
     * @param requestKey the resource key for the request
     * @return a string containing the localized text for the request
     */
    private StringBuilder translateQuestion(String requestKey) {
        return new StringBuilder()
                .append(R.string(requestKey))
                .append(lineSeparator);
    }

    public void setController(ClientController controller) {
        this.controller = controller;
        askConnection();
    }
}
