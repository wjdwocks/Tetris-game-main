package Menu;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class ScoreBoard3 extends JPanel implements KeyListener {
    public final JLabel mainLabel;
    private final String[] labels = {"1st", "2nd", "3rd", "4rd", "5rd", "6rd", "7rd", "8rd"}; // 메인 메뉴에 있을 서브 메뉴들.
    private JLabel keyMessage;
    private javax.swing.Timer messageTimer;
    JSONArray sortedScoreArray;
    JSONParser scoreParser;

    public ScoreBoard3() {
        this.setSize(Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2]);
        this.setLayout(null);

        mainLabel = new JLabel();
        mainLabel.setSize(Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2]);

        JLabel title = new JLabel("Score Board");
        title.setFont(new Font("Arial", Font.BOLD, Main.SCREEN_WIDTH[2] / 40)); // 폰트 설정
        title.setForeground(Color.BLACK); // 텍스트 색상 설정
        title.setBounds(Main.SCREEN_WIDTH[2]/2 - 200, Main.SCREEN_HEIGHT[2] / 20, 400, 50); // 위치와 크기 설정
        title.setHorizontalAlignment(JLabel.CENTER);
        mainLabel.add(title);

        ////////////////////////////////////////// 점수 데이터 가져오기//////////////////////////////////
        scoreParser = new JSONParser();

        try (FileReader reader = new FileReader("Tetris_game-main/Tetris_game/src/scoreData.json")) {
            // 파일로부터 JSON 배열을 읽어오기
            JSONArray scoreArray = (JSONArray) scoreParser.parse(reader);

            // JSONArray를 List<JSONObject>로 받아오기
            java.util.List<JSONObject> scoreList = new ArrayList<>();
            for (Object item : scoreArray) {
                scoreList.add((JSONObject) item);
            }

            // 스코어(scores)에 따라 List<JSONObject> 정렬
            Collections.sort(scoreList, new Comparator<JSONObject>() {
                public int compare(JSONObject a, JSONObject b) {
                    long valA = (long) a.get("scores"); // 'scores' 값을 long으로 캐스팅
                    long valB = (long) b.get("scores");
                    return Long.compare(valB, valA);
                }
            });

            // 정렬된 List<JSONObject>를 JSONArray로 다시 변환
            sortedScoreArray = new JSONArray();
            for (JSONObject scoreData : scoreList) {
                sortedScoreArray.add(scoreData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 이미 있는 이 함수를 통해 점수를 보여줄까? 고민중
        int Start_x = 0;
        int Start_y = Main.SCREEN_HEIGHT[2] / 3;
        for(int i=0;i<4;i++)
        {
            addMenuItem(String.format("%s : %.5s, Mode : %s, Score : %s",
                    labels[i],
                    (((JSONObject)sortedScoreArray.get(i)).get("name")).toString(),
                    (((JSONObject)sortedScoreArray.get(i)).get("mode")).toString().equals("1") ? "Normal" : "Item",
                    (((JSONObject)sortedScoreArray.get(i)).get("scores"))).toString(), Start_x, Start_y);
            Start_y += Main.SCREEN_HEIGHT[2] / 18;
        }
        Start_x = Main.SCREEN_WIDTH[2] / 2;
        Start_y = Main.SCREEN_HEIGHT[2] / 3;
        for(int i=4;i<8;i++)
        {
            addMenuItem(String.format("%s : %.5s, Mode : %s, Score : %s",
                    labels[i],
                    (((JSONObject)sortedScoreArray.get(i)).get("name")).toString(),
                    (((JSONObject)sortedScoreArray.get(i)).get("mode")).toString().equals("1") ? "Normal" : "Item",
                    (((JSONObject)sortedScoreArray.get(i)).get("scores")).toString()), Start_x, Start_y);
            Start_y += Main.SCREEN_HEIGHT[2] / 18;
        }

        keyMessage = new JLabel(" ");
        keyMessage.setFont(new Font("Arial", Font.BOLD, Main.SCREEN_WIDTH[2] / 30)); // 폰트 설정
        keyMessage.setForeground(Color.BLACK); // 텍스트 색상 설정
        keyMessage.setBounds(Main.SCREEN_WIDTH[2]/4, Main.SCREEN_HEIGHT[0] / 7, 800, Main.SCREEN_WIDTH[2]/13); // 위치와 크기 설정

        add(keyMessage);

        messageTimer = new javax.swing.Timer(700, e -> keyMessage.setVisible(false));
        messageTimer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정

        add(mainLabel);

        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == ((Number) (Main.SettingObject.get("K_ENTER"))).intValue())
            switchToScreen(Main.optionMenu3);
        else
            showTemporaryMessage("<html>Invalid Key Input. <br>If you want to exit, Press Enter</html>");
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void addMenuItem(String text, int x, int y) {
        JLabel menuItem = new JLabel(text);
        menuItem.setFont(new Font("Arial", Font.BOLD, Main.SCREEN_HEIGHT[2] / 24)); // 폰트 설정
        menuItem.setForeground(Color.BLACK); // 텍스트 색상 설정
        menuItem.setBounds(x, y, Main.SCREEN_WIDTH[2] / 2, Main.SCREEN_HEIGHT[2] / 12); // 위치와 크기 설정
        mainLabel.add(menuItem);
    }

    public void switchToScreen(JPanel newScreen) {
        Main.cardLayout.show(Main.mainPanel, newScreen.getName()); // 화면 전환
        System.out.println(newScreen.getName());
        newScreen.setFocusable(true); // 새 화면이 포커스를 받을 수 있도록 설정
        newScreen.requestFocusInWindow(); // 새 화면에게 포커스 요청
    }

    private void showTemporaryMessage(String message)
    { // 화면에 키입력 메시지를 띄움
        keyMessage.setText(message); // 메시지 표시
        keyMessage.setVisible(true); // 라벨을 보이게 설정
        messageTimer.restart(); // 타이머 시작 (이전 타이머가 실행 중이었다면 재시작)
    }

}
