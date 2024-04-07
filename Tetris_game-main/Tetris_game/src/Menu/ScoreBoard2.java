package Menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ScoreBoard2 extends JPanel implements KeyListener {
    public final JLabel mainLabel;
    private final String[] labels = new String[]{"1st", "2nd", "3rd", "4rd", "5rd", "6rd", "7rd", "8rd"};
    private JLabel keyMessage;
    private Timer messageTimer;
    JSONArray sortedScoreArray;
    JSONParser scoreParser;

    public ScoreBoard2() {
        this.setSize(Main.SCREEN_WIDTH[1], Main.SCREEN_HEIGHT[1]);
        this.setLayout((LayoutManager)null);
        this.mainLabel = new JLabel();
        this.mainLabel.setSize(Main.SCREEN_WIDTH[1], Main.SCREEN_HEIGHT[1]);
        JLabel title = new JLabel("Score Board");
        title.setFont(new Font("Arial", 1, Main.SCREEN_WIDTH[1] / 40));
        title.setForeground(Color.BLACK);
        title.setBounds(Main.SCREEN_WIDTH[1] / 2 - 200, Main.SCREEN_HEIGHT[1] / 20, 400, 50);
        title.setHorizontalAlignment(0);
        this.mainLabel.add(title);
        this.scoreParser = new JSONParser();

        try (FileReader reader = new FileReader("Tetris_game-main/Tetris_game/src/scoreData.json")) {
            // 파일로부터 JSON 배열을 읽어오기
            JSONArray scoreArray = (JSONArray) scoreParser.parse(reader);

            // JSONArray를 List<JSONObject>로 변환
            List<JSONObject> scoreList = new ArrayList<>();
            for (Object item : scoreArray) {
                scoreList.add((JSONObject) item);
            }

            // 스코어(scores)에 따라 List<JSONObject> 정렬
            scoreList.sort((a, b) -> {
                long valA = (long) a.get("scores");
                long valB = (long) b.get("scores");
                return Long.compare(valB, valA);
            });

            // 정렬된 List<JSONObject>를 JSONArray로 다시 변환
            sortedScoreArray = new JSONArray();
            for (JSONObject score : scoreList) {
                sortedScoreArray.add(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int Start_x = 0;
        int Start_y = Main.SCREEN_HEIGHT[1] / 3;

        int i;
        for(i = 0; i < 4; ++i) {
            this.addMenuItem(String.format("%s : %.5s, Mode : %s, Score : %s", this.labels[i],
                    ((JSONObject)this.sortedScoreArray.get(i)).get("name").toString(),
                    ((JSONObject)this.sortedScoreArray.get(i)).get("mode").toString().equals("1") ? "Normal" : "Item",
                    ((JSONObject)this.sortedScoreArray.get(i)).get("scores").toString()), Start_x, Start_y);
            Start_y += Main.SCREEN_HEIGHT[1] / 18;
        }

        Start_x = Main.SCREEN_WIDTH[1] / 2;
        Start_y = Main.SCREEN_HEIGHT[1] / 3;

        for(i = 4; i < 8; ++i) {
            this.addMenuItem(String.format("%s : %.5s, Mode : %s, Score : %s", this.labels[i], ((JSONObject)this.sortedScoreArray.get(i)).get("name").toString(), ((JSONObject)this.sortedScoreArray.get(i)).get("mode").toString().equals("1") ? "Normal" : "Item", ((JSONObject)this.sortedScoreArray.get(i)).get("scores").toString()), Start_x, Start_y);
            Start_y += Main.SCREEN_HEIGHT[1] / 18;
        }
        this.keyMessage = new JLabel(" ");
        this.keyMessage.setFont(new Font("Arial", 1, Main.SCREEN_WIDTH[1] / 30));
        this.keyMessage.setForeground(Color.BLACK);
        this.keyMessage.setBounds(Main.SCREEN_WIDTH[1] / 4, Main.SCREEN_HEIGHT[1] / 7, 800, Main.SCREEN_WIDTH[1] / 13);
        this.add(this.keyMessage);
        this.messageTimer = new Timer(700, (e) -> {
            this.keyMessage.setVisible(false);
        });
        this.messageTimer.setRepeats(false);
        this.add(this.mainLabel);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setVisible(true);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == ((Number)Main.SettingObject.get("K_ENTER")).intValue()) {
            this.switchToScreen(Main.optionMenu2);
        } else {
            this.showTemporaryMessage("<html>Invalid Key Input. <br>If you want to exit, Press Enter</html>");
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public void addMenuItem(String text, int x, int y) {
        JLabel menuItem = new JLabel(text);
        menuItem.setFont(new Font("Arial", 1, Main.SCREEN_HEIGHT[1] / 24));
        menuItem.setForeground(Color.BLACK);
        menuItem.setBounds(x, y, Main.SCREEN_WIDTH[1] / 2, Main.SCREEN_HEIGHT[1] / 12);
        this.mainLabel.add(menuItem);
    }

    public void switchToScreen(JPanel newScreen) {
        Main.cardLayout.show(Main.mainPanel, newScreen.getName());
        System.out.println(newScreen.getName());
        newScreen.setFocusable(true);
        newScreen.requestFocusInWindow();
    }

    private void showTemporaryMessage(String message) {
        this.keyMessage.setText(message);
        this.keyMessage.setVisible(true);
        this.messageTimer.restart();
    }
}
