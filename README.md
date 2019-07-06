# Prova Finale Ingegneria del Software 2019
## Gruppo AM45

- ###   10538950    Giulio Antonio Abbo ([@poligiulio](https://github.com/poligiulio))<br>giulioantonio.abbo@mail.polimi.it
- ###   10539533    Silvio Attolini ([@SilvioAttolini](https://github.com/SilvioAttolini))<br>silvio.attolini@mail.polimi.it
- ###   10549663    Fahed Ben Tej ([@GetnaG](https://github.com/GetnaG))<br>fahed.ben@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| RMI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)|
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

## Use Guide
### Setting
Go to `./src/main/resources/properties` and open the `settings.properties` file.  <br>
Here you can set your preferred network and game settings. <br>
### Build 
Run the following line usign maven to build the server and client jar: <br>
`mvn -Dmaven.test.skip=true package  -quiet` <br>
### Run
Extract the zip. <br>
To run the client: <br>
GUI : `java --module-path ./javafx-sdk-12/lib --add-modules javafx.controls,javafx.fxml -jar ./deliverables/JARs/client.jar gui` <br>
CLI : `java -jar ./deliverables/JARs/client.jar cli` <br>
To run the server: <br>
`java -jar ./deliverables/JARs/server.jar` <br>

