package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.clientlogic.PlayerState;
import it.polimi.ingsw.client.resources.R;

import java.io.*;
import java.util.*;

/**
 * Command line interface, handles the communication with the user via text.
 * This will show the user the status of the match, the last notification,
 * the current request if there is one and whether this is waiting for input.
 * <p>
 * The match status is read from the model; this is also subscribed to the
 * model, so that it can be notified when changes occur.
 *
 * @author giubots
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
     * follows a series of constants used in this class
     */
    private static final int BOARD_LENGTH = 12;
    private static final int MARKET_SIZE = 3;
    private static final int BLUE_SPAWN_POINT = 2;
    private static final int RED_SPAWN_POINT = 4;
    private static final int YELLOW_SPAWN_POINT = 11;
    private static final int DAMAGE_TILE = 10;
    private static final int KILLSHOT_TILE = 10; //the 11th
    private static final int OVERKILL_TILE = 11; //the 12th

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
            if ("socket".equals(connection)) {
                try {
                    controller.setSocket(
                            handleQuestion("chooseIP", null),
                            Integer.parseInt(handleQuestion("choosePort", null))
                    );
                } catch (IOException | NumberFormatException e) {
                    handleNotification("socketError");
                    System.exit(-1);
                }
            } else if ("rmi".equals(connection)) {
                try {
                    controller.setRmi(
                            handleQuestion("chooseIP", null));
                } catch (IOException e) {
                    handleNotification("rmiError");
                    System.exit(-1);
                }
            } else {
                handleNotification("connectionError");
                System.exit(-1);
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
        refresh();
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

            Properties properties = R.properties("settings");
            String defaultHost = properties.getProperty("host");
            String defaultPort = properties.getProperty("clientSocketPort");

            if (requestKey.equals("chooseIP") && input.length() == 0)
                return defaultHost;
            if (requestKey.equals("choosePort") && input.length() == 0)
                return defaultPort;
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
     * language.
     *
     * @return a string containing the status to be displayed
     */
    private StringBuilder translateStatus() {
        StringBuilder stringBuilder = new StringBuilder();

        if (this.model != null) {
            if (model.getPlayersState() != null ) {
                if(!model.getPlayersState().isEmpty() )
                    if(model.getPlayersState().get(0) != null ){
                            if(model.getPlayersState().get(0).getSkullNumber() >= 0)
                                drawConfigurationAndSquares(stringBuilder);
                            drawBoards(stringBuilder);
                    }
            }
        }
        return stringBuilder;
    }

    /**
     * @param s is the printed CLI updated with the chosen configuration and the consequent squares
     */
    private void drawConfigurationAndSquares(StringBuilder s) {
        Integer[] forbiddenSquares = {100, 100};
        switch (model.getConfigurationID()) {
            case 0: {
                forbiddenSquares[0] = 3;
                break;
            }
            case 1: {
                forbiddenSquares[0] = 3;
                forbiddenSquares[1] = 8;
                break;
            }
            case 2: {
                forbiddenSquares[0] = 8;
                break;
            }
            case 3: {
                break;
            }
            default:
        }

        for (int i = 0; i < BOARD_LENGTH; i++) {
            drawLayer(s, model.getConfigurationID(), i);
            drawAmmosOrWeaponsAndPlayers(s, forbiddenSquares, i);
        }

        switch (model.getConfigurationID()) {
            case 0: {
                s.append(R.string("config_line_012"));
                break;
            }
            case 1: {
                s.append("      ").append(R.string("config_line_112"));
                break;
            }
            case 2: {
                s.append("      ").append(R.string("config_line_212"));
                break;
            }
            case 3: {
                s.append(R.string("config_line_312"));
                break;
            }
            default:
        }

        s.append(lineSeparator);
    }

    /**
     * @param layer is the printed CLI updated
     * @param config is the chosen configuration
     * @param line indicates the current position (map seen as a linear vector)
     */
    private void drawLayer(StringBuilder layer, int config, int line) {
        switch (config) {
            case 0:
                switch (line) {
                    case 0: {
                        layer.append(R.string("config_line_00"));
                        break;
                    }
                    case 1: {
                        layer.append(R.string("config_line_01"));
                        break;
                    }
                    case 2: {
                        layer.append(R.string("config_line_02"));
                        break;
                    }
                    case 3: {
                        layer.append(R.string("config_line_03"));
                        break;
                    }
                    case 4: {
                        layer.append(R.string("config_line_04"));
                        break;
                    }
                    case 5: {
                        layer.append(R.string("config_line_05"));
                        break;
                    }
                    case 6: {
                        layer.append(R.string("config_line_06"));
                        break;
                    }
                    case 7: {
                        layer.append(R.string("config_line_07"));
                        break;
                    }
                    case 8: {
                        layer.append(R.string("config_line_08"));
                        break;
                    }
                    case 9: {
                        layer.append(R.string("config_line_09"));
                        break;
                    }
                    case 10: {
                        layer.append(R.string("config_line_010"));
                        break;
                    }
                    case 11: {
                        layer.append(R.string("config_line_011"));
                        break;
                    }
                    default:
                }
                break;
            case 1:
                switch (line) {
                    case 0: {
                        layer.append(R.string("config_line_10"));
                        break;
                    }
                    case 1: {
                        layer.append(R.string("config_line_11"));
                        break;
                    }
                    case 2: {
                        layer.append(R.string("config_line_12"));
                        break;
                    }
                    case 3: {
                        layer.append(R.string("config_line_13"));
                        break;
                    }
                    case 4: {
                        layer.append(R.string("config_line_14"));
                        break;
                    }
                    case 5: {
                        layer.append(R.string("config_line_15"));
                        break;
                    }
                    case 6: {
                        layer.append(R.string("config_line_16"));
                        break;
                    }
                    case 7: {
                        layer.append(R.string("config_line_17"));
                        break;
                    }
                    case 8: {
                        layer.append(R.string("config_line_18"));
                        break;
                    }
                    case 9: {
                        layer.append("      ").append(R.string("config_line_19"));
                        break;
                    }
                    case 10: {
                        layer.append("      ").append(R.string("config_line_110"));
                        break;
                    }
                    case 11: {
                        layer.append("      ").append(R.string("config_line_111"));
                        break;
                    }
                    default:
                }
                break;
            case 2:
                switch (line) {
                    case 0: {
                        layer.append(R.string("config_line_20"));
                        break;
                    }
                    case 1: {
                        layer.append(R.string("config_line_21"));
                        break;
                    }
                    case 2: {
                        layer.append(R.string("config_line_22"));
                        break;
                    }
                    case 3: {
                        layer.append(R.string("config_line_23"));
                        break;
                    }
                    case 4: {
                        layer.append(R.string("config_line_24"));
                        break;
                    }
                    case 5: {
                        layer.append(R.string("config_line_25"));
                        break;
                    }
                    case 6: {
                        layer.append(R.string("config_line_26"));
                        break;
                    }
                    case 7: {
                        layer.append(R.string("config_line_27"));
                        break;
                    }
                    case 8: {
                        layer.append(R.string("config_line_28"));
                        break;
                    }
                    case 9: {
                        layer.append("      ").append(R.string("config_line_29"));
                        break;
                    }
                    case 10: {
                        layer.append("      ").append(R.string("config_line_210"));
                        break;
                    }
                    case 11: {
                        layer.append("      ").append(R.string("config_line_211"));
                        break;
                    }
                    default:
                }
                break;
            case 3:
                switch (line) {
                    case 0: {
                        layer.append(R.string("config_line_30"));
                        break;
                    }
                    case 1: {
                        layer.append(R.string("config_line_31"));
                        break;
                    }
                    case 2: {
                        layer.append(R.string("config_line_32"));
                        break;
                    }
                    case 3: {
                        layer.append(R.string("config_line_33"));
                        break;
                    }
                    case 4: {
                        layer.append(R.string("config_line_34"));
                        break;
                    }
                    case 5: {
                        layer.append(R.string("config_line_35"));
                        break;
                    }
                    case 6: {
                        layer.append(R.string("config_line_36"));
                        break;
                    }
                    case 7: {
                        layer.append(R.string("config_line_37"));
                        break;
                    }
                    case 8: {
                        layer.append(R.string("config_line_38"));
                        break;
                    }
                    case 9: {
                        layer.append(R.string("config_line_39"));
                        break;
                    }
                    case 10: {
                        layer.append(R.string("config_line_310"));
                        break;
                    }
                    case 11: {
                        layer.append(R.string("config_line_311"));
                        break;
                    }
                    default:
                }
                break;
            default:
        }
    }

    /**
     * @param ammoWeaponPlayersLayer is the printed CLI updated
     * @param f represents the squares excluded from the map by the configuration
     * @param line indicates the current position (map seen as a linear vector)
     */
    private void drawAmmosOrWeaponsAndPlayers(StringBuilder ammoWeaponPlayersLayer, Integer[] f, int line) {
        if (line != f[0] && line != f[1]) {
            ammoWeaponPlayersLayer.append(" - square ").append(line).append(": ");
            if (model.getAmmoCardsID() != null) {
                if(line == RED_SPAWN_POINT || line == BLUE_SPAWN_POINT || line == YELLOW_SPAWN_POINT )
                    drawMarket(line, ammoWeaponPlayersLayer);
                    /*ammoWeaponPlayersLayer.append(R.string("market"));*/
                else if (model.getAmmoCardsID().get(line) != null && !model.getAmmoCardsID().get(line).isEmpty())
                    drawAmmo(ammoWeaponPlayersLayer, line);
                /*else {
                    drawMarket(line, ammoWeaponPlayersLayer);
                }*/
            }

            drawPlayersInTheSquare(ammoWeaponPlayersLayer, line);
        }
        else
            ammoWeaponPlayersLayer.append(lineSeparator);

    }

    /**
     * @param ammoLine is the printed CLI updated
     * @param l indicates the current position (map seen as a linear vector)
     */
    private void drawAmmo(StringBuilder ammoLine, int l) {
        ammoLine.append("ammo: [ ").append(R.string(model.getAmmoCardsID().get(l))).append(" ]");
    }

    /**
     * @param spawnP indicates the current position (map seen as a linear vector)
     * @param marketLine is the printed CLI updated
     */
    private void drawMarket(int spawnP, StringBuilder marketLine) {
        marketLine.append("market: < ");
        switch (spawnP) {
            case BLUE_SPAWN_POINT: {
                for (int j = 0; j < MARKET_SIZE; j++) {
                    if (model.getWeaponsCardsID().get(j) != null)
                        marketLine.append(R.string(model.getWeaponsCardsID().get(j)));
                    if (j < 2)
                        marketLine.append(", ");
                }
                break;
            }
            case RED_SPAWN_POINT: {
                for (int j = 0; j < MARKET_SIZE; j++) {
                    if (model.getWeaponsCardsID().get(j + MARKET_SIZE) != null)
                        marketLine.append(R.string(model.getWeaponsCardsID().get(j + MARKET_SIZE)));
                    if (j < 2)
                        marketLine.append(", ");
                }
                break;
            }
            case YELLOW_SPAWN_POINT: {
                for (int j = 0; j < MARKET_SIZE; j++) {
                    if (model.getWeaponsCardsID().get(j + 2 * MARKET_SIZE) != null)
                        marketLine.append(R.string(model.getWeaponsCardsID().get(j + 2 * MARKET_SIZE)));
                    if (j < 2)
                        marketLine.append(", ");
                }
                break;
            }

            default:
        }
        marketLine.append(" >");
    }

    /**
     * @param sPlayers is the printed CLI updated
     * @param l indicates the current position (map seen as a linear vector)
     */
    private void drawPlayersInTheSquare(StringBuilder sPlayers, int l) {
        int pos;
        List<PlayerState> playersHere = new ArrayList<>();
        if (model.getPlayersState() != null && !model.getPlayersState().isEmpty()) {
            for (PlayerState ps : model.getPlayersState()) {
                pos = ps.getSquarePosition();
                if (pos == l)
                    playersHere.add(ps);
            }
            sPlayers.append(" players: { ");
            if (!playersHere.isEmpty()) {
                for (PlayerState ps : playersHere) {
                        sPlayers.append(ps.getNickname());
                        if (playersHere.indexOf(ps) < playersHere.size()-1)
                            sPlayers.append(", ");
                }
            }
            sPlayers.append(" }");

        }

        sPlayers.append(lineSeparator);
    }

    /**
     * @param str is the printed CLI updated
     */
    private void drawBoards(StringBuilder str) {

        for (PlayerState ps : model.getPlayersState()) {
            str.append("/---------------------------").append(lineSeparator).append("| ");
            drawPlayersStatus(str, ps);
            str.append("| ");
            drawDamageKillshotOverkill(str, ps);
            drawMarksAndSkulls(str, ps);
            drawBoardValue(str, ps);
            str.append("| ");
            drawCubes(str, ps);
            drawWeapons(str, ps);
            drawPowerups(str, ps);
            str.append("\\---------------------------").append(lineSeparator);
        }

        if (model.getKillshotTrack() != null)
            drawCurrentKillshotTrack(str, model.getKillshotTrack());

        drawTurnPlayerFrenzyPlayersFrenzyTurn(str);

        str.append(lineSeparator);

        if(model.getWinners() != null && !model.getWinners().isEmpty()) {
            str.append("winners: ");
            for (String s : model.getWinners()) {
                str.append(model.getWinners().indexOf(s) + 1).append(") ")
                        .append(s).append(lineSeparator).append("         ");
            }
        }

    }

    /**
     * @param temp is the printed CLI updated
     * @param p represents the single player's current state
     */
    private void drawPlayersStatus(StringBuilder temp, PlayerState p) {
        if (p.isConnected())
            temp.append("< ").append(p.getNickname()).append(": online >").append(lineSeparator);
        else
            temp.append("< ").append(p.getNickname()).append(": offline >").append(lineSeparator);

    }

    /**
     * @param temp is the printed CLI updated
     * @param p represents the single player's current state
     */
    private void drawDamageKillshotOverkill(StringBuilder temp, PlayerState p) {
        temp.append("damage dealt by: ").append(p.getDamage());
        for (String s : p.getDamage()) {
            if (p.getDamage().size() >= DAMAGE_TILE && p.getDamage().indexOf(s) == KILLSHOT_TILE){
                temp.append(" -|- ");
               temp.append("killshot by: ").append(s);
            }
            if (p.getDamage().size() >= DAMAGE_TILE && p.getDamage().indexOf(s) == OVERKILL_TILE){
                temp.append(" -|- ");
                temp.append("overkill by: ").append(s);
            }
        }

        temp.append(" -|- ");
    }

    /**
     * @param temp is the printed CLI updated
     * @param p represents the single player's current state
     */
    private void drawMarksAndSkulls(StringBuilder temp, PlayerState p) {
        temp.append("passive marks by: ");
        if(p.getMarks().isEmpty())
            temp.append("none");
        else {
            temp.append("[ ");
            for (String s : p.getMarks()) {
                temp.append(s);
                if (p.getMarks().indexOf(s) < p.getMarks().size() - 1)
                    temp.append(", ");
            }
            temp.append(" ]");
        }

        temp.append(" -|- ");

        if (p.getSkullNumber() >= 0)
            temp.append("skulls: ").append(p.getSkullNumber());
        else
            temp.append("skulls: 0");

        temp.append(" -|- ");

    }

    /**
     * @param temp is the printed CLI updated
     * @param p represents the single player's current state
     */
    private void drawBoardValue(StringBuilder temp, PlayerState p) {
        Integer[] vectorValue = {8, 6, 4, 2, 1, 1, 1, 1, 1};
        temp.append("board value: { ");
        if (p.getSkullNumber() >= 0)
            temp.append(vectorValue[p.getSkullNumber()]);
        else
            temp.append(vectorValue[0]);
        temp.append(" }");

        temp.append(lineSeparator);

    }

    /**
     * @param temp is the printed CLI updated
     * @param p represents the single player's current state
     */
    private void drawCubes(StringBuilder temp, PlayerState p) {
        temp.append("ammo cubes: ");
        if (p.getAmmoCubes() != null && !p.getAmmoCubes().isEmpty()) {
            if (p.getAmmoCubes().get(0) != null && !p.getAmmoCubes().isEmpty() && p.getAmmoCubes().get(0) > 0)
                temp.append("[").append(p.getAmmoCubes().get(0)).append(" BLUE] ");
            if (p.getAmmoCubes().get(1) != null && p.getAmmoCubes().size() > 1 && p.getAmmoCubes().get(1) > 0)
                temp.append("[").append(p.getAmmoCubes().get(1)).append(" RED] ");
            if (p.getAmmoCubes().get(2) != null && p.getAmmoCubes().size() > 2 && p.getAmmoCubes().get(2) > 0)
                temp.append("[").append(p.getAmmoCubes().get(2)).append(" YELLOW]");
        }
        else
            temp.append("none");

        temp.append(" -|- ");

    }

    /**
     * @param temp is the printed CLI updated
     * @param p represents the single player's current state
     */
    private void drawWeapons(StringBuilder temp, PlayerState p) {
        //loaded weapons
        temp.append("loaded weapons: ");
        if(p.getLoadedWeapons().isEmpty())
            temp.append("none");
        else
            for (String s:  p.getLoadedWeapons())
                temp.append("[ ").append(R.string(s)).append(" ]");

        if(p.getLoadedWeapons().size() > 2)
            temp.append(lineSeparator).append("| ");
        else
            temp.append(" -|- ");

        //unloaded weapons
        temp.append("unloaded weapons: ");
        if(p.getUnloadedWeapons().isEmpty())
            temp.append("none");
        else
            for (String s: p.getUnloadedWeapons())
                temp.append("[ ").append(R.string(s)).append(" ]");

        if(p.getUnloadedWeapons().size() > 2 && p.getPowerups().size() > 1)
            temp.append(lineSeparator).append("| ");
        else
            temp.append(" -|- ");

    }

    /**
     * @param temp is the printed CLI updated
     * @param p represents the single player's current state
     */
    private void drawPowerups(StringBuilder temp, PlayerState p) {
        if (p.getPowerups().size() > 2)
            temp.append(lineSeparator).append("| ");
        temp.append("powerups: ");
        if(p.getPowerups().isEmpty())
            temp.append("none");
        else {
            for (String s : p.getPowerups())
                temp.append("[ ").append(R.string(s)).append(" ]");
        }

        temp.append(lineSeparator);

    }

    /**
     * @param temp is the printed CLI updated
     */
    private void drawTurnPlayerFrenzyPlayersFrenzyTurn(StringBuilder temp) {
        int skullsRemoved = 0;

        for (PlayerState x : model.getPlayersState()) {
            if (x.isPlayerBoardFrenzy())
                temp.append(x.getNickname()).append(" is in Frenzy Mode!").append(lineSeparator);
            skullsRemoved += x.getSkullNumber();
        }

        if (skullsRemoved == 8) {
            temp.append("--- FRENZY TURN STARTED! ---").append(lineSeparator);
        }

        temp.append(lineSeparator);

        for (PlayerState x : model.getPlayersState())
            if (x.isCurrent())
                temp.append("-> it's ").append(x.getNickname()).append("'s turn!").append(lineSeparator);


    }

    /**
     * @param temp is the printed CLI updated
     * @param kst is the current Killshot Track
     */
    private void drawCurrentKillshotTrack(StringBuilder temp, List<List<String>> kst) {
        temp.append(lineSeparator);
        if(!kst.isEmpty()) {
            temp.append("current Killshot track: ");
            for (int i = 0; i < kst.size(); i++)
                for (String q : kst.get(i)) {
                    temp.append(i).append(") ").append(q).append(lineSeparator);
                    if (i < kst.size() - 1)
                        temp.append("                        ");
                }
            temp.append(lineSeparator);
        }
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
            builder.append(i).append(CHOICE_INDEX);
            String next = iterator.next();
            try {builder.append(R.string(next));}
            catch (MissingResourceException e) {
                /*No resource found: printing the key*/
                builder.append(next);
            }

            while (iterator.hasNext()) {
                builder.append(R.string("choiceSeparator")).append(SPACE);
                try {
                    builder.append(R.string(iterator.next()));
                } catch (MissingResourceException e) {
                    /*No resource found: printing the key*/
                    builder.append(next);
                }
            }
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

    /**
     * @param controller the controller to set
     */
    public void setController(ClientController controller) {
        this.controller = controller;
        askConnection();
    }

}
