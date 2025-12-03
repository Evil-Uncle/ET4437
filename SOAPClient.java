ackage com.mycompany.tttgame;
 
import com.tttws.TicTacToeWS;
import com.tttws.TicTacToeWebService;
 
/**
* SOAP Client wrapper for TicTacToeWS
* Matches the generated TicTacToeWS interface exactly.
*/
public class SOAPClient {
 
    // Static service + proxy (step 7)
    private static TicTacToeWebService service;
    private static TicTacToeWS proxy;
 
    // Constructor initializes the SOAP connection once
    public SOAPClient() {
        if (service == null || proxy == null) {
            service = new TicTacToeWebService();
            proxy = service.getTicTacToeWSPort();
        }
    }
 
    // Optional: let other classes access the raw proxy if needed
    public static TicTacToeWS getProxy() {
        if (proxy == null) {
            service = new TicTacToeWebService();
            proxy = service.getTicTacToeWSPort();
        }
        return proxy;
    }
 
    // ---------------------------
    // WRAPPER METHODS
    // These signatures & types must match TicTacToeWS.java
    // ---------------------------
 
    // login(username, password) -> int
    public int login(String username, String password) {
        return proxy.login(username, password);
    }
 
    // register(username, password, name, surname) -> String
    public String register(String username, String password,
                           String name, String surname) {
        return proxy.register(username, password, name, surname);
    }
 
    // newGame(uid) -> String
    public String newGame(int uid) {
        return proxy.newGame(uid);
    }
 
    // joinGame(uid, gid) -> String
    public String joinGame(int uid, int gid) {
        return proxy.joinGame(uid, gid);
    }
 
    // getBoard(gid) -> String
    public String getBoard(int gid) {
        return proxy.getBoard(gid);
    }
 
    // checkSquare(x, y, gid) -> String
    public String checkSquare(int x, int y, int gid) {
        return proxy.checkSquare(x, y, gid);
    }
 
    // takeSquare(x, y, gid, pid) -> String
    public String takeSquare(int x, int y, int gid, int pid) {
        return proxy.takeSquare(x, y, gid, pid);
    }
 
    // checkWin(gid) -> String
    public String checkWin(int gid) {
        return proxy.checkWin(gid);
    }
 
    // getGameState(gid) -> String
    public String getGameState(int gid) {
        return proxy.getGameState(gid);
    }
 
    // setGameState(gid, gstate) -> String
    public String setGameState(int gid, int gstate) {
        return proxy.setGameState(gid, gstate);
    }
 
    // deleteGame(gid, uid) -> String
    public String deleteGame(int gid, int uid) {
        return proxy.deleteGame(gid, uid);
    }
 
    // showMyOpenGames(uid) -> String
    public String showMyOpenGames(int uid) {
        return proxy.showMyOpenGames(uid);
    }
 
    // showAllMyGames(uid) -> String
    public String showAllMyGames(int uid) {
        return proxy.showAllMyGames(uid);
    }
 
    // showOpenGames() -> String
    public String showOpenGames() {
        return proxy.showOpenGames();
    }
 
    // leagueTable() -> String
    public String leagueTable() {
        return proxy.leagueTable();
    }
 
    // closeConnection() -> void
    public void closeConnection() {
        proxy.closeConnection();
    }
}

