package sample;

import POJO.Params;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.Buffer;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //объект с переменными и методами ЭЦП
    Params params = new Params();
    //режим: false - автомат, true - ручной
    Boolean mod = false;

    //поля в гуи
    @FXML
    TextField p;
    @FXML
    TextField a;
    @FXML
    TextField yai;
    @FXML
    TextField x;
    @FXML
    TextField y;
    @FXML
    TextField m;
    @FXML
    TextField z;
    @FXML
    TextField g;
    @FXML
    TextField k;
    @FXML
    TextField s;
    @FXML
    TextField right;
    @FXML
    TextField left;

    //кнопки в гуи
    @FXML
    Button generate1;
    @FXML
    Button generate2;
    @FXML
    Button generate3;

    @Override
    //при инициализации отключаем поля которые вычисляются
    public void initialize(URL location, ResourceBundle resources) {
        y.setDisable(true);
        z.setDisable(true);
        g.setDisable(true);
        k.setDisable(true);
        s.setDisable(true);
        right.setDisable(true);
        left.setDisable(true);
    }

    @FXML
    //кнопка с добавлением p, y`, a
    public void generate1(){
        p.setStyle("-fx-border-color: black ; -fx-border-width: 1px ;");
        a.setStyle("-fx-border-color: black; -fx-border-width: 1px ;");
        yai.setStyle("-fx-border-color: black ; -fx-border-width: 1px ;");
        //если режим генерации
        if (!mod){
            params.initStartParams();
        }else {
            //если режим ручного ввода можно внести некорректные значения
            try {
                //пытаемся ввести
                params.initStartParams(
                        new BigInteger(p.getText()),
                        new BigInteger(a.getText()),
                        new BigInteger(yai.getText())
                );
                //если значения неккоректны делаем красную рамку
            }catch (ArithmeticException | NumberFormatException ex){
                p.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                a.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                yai.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
        }
        //обновление полей
        update();
    }

    @FXML
    //кнопка с добавлением X и вычислением Y
    public void generate2(){
        x.setStyle("-fx-border-color: black ; -fx-border-width: 1px ;");
        //автомат
        if (!mod){
            params.initXandY();
        }else {
            //ручной
            try {
                params.initXandY(
                        new BigInteger(x.getText())
                );
                //если ввели бред то красные рамки
            }catch (ArithmeticException | NumberFormatException ex){
                x.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
        }
        //обновить поля
        update();
    }
    @FXML
    //кнопка с добавлением хэша/сообщения и вычислением всех последующих параметров
    public void generate3() {
        m.setStyle("-fx-border-color: black ; -fx-border-width: 1px ;");
        //автомат
        if (!mod){
            params.initHash();
        }else {
            //ручной
            try {
                params.initHash(
                        new BigInteger(m.getText())
                );
                //если слишком большая длинна то красные рамки
            }catch (ArithmeticException | NumberFormatException ex){
                m.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
        }
        //обновить поля
        update();
    }
    @FXML
    public void check(){
        //проверить подпись. По сути просто сравнивает левую и правую часть
        params.checkSign();
        //обновить поля
        update();
    }

    private void update(){
        //обновляет поля
        p.setText(params.getP() == null ? " " : params.getP().toString());
        a.setText(params.getA() == null ? " " : params.getA().toString());
        yai.setText(params.getGamma() == null ? " " : params.getGamma().toString());
        x.setText(params.getX() == null ? " " : params.getX().toString());
        y.setText(params.getY() == null ? " " : params.getY().toString());
        m.setText(params.getU() == null ? " " : params.getU().toString());
        z.setText(params.getZ() == null ? " " : params.getZ().toString());
        g.setText(params.getG() == null ? " " : params.getG().toString());
        k.setText(params.getK() == null ? " " : params.getK().toString());
        s.setText(params.getS() == null ? " " : params.getS().toString());
        right.setText(params.getRight() == null ? " " : params.getRight().toString());
        left.setText(params.getLeft() == null ? " " : params.getLeft().toString());
    }

    @FXML
    //кнопка со сменой режима
    public void changeMod(){
        //инфвертируем режим
        mod = !mod;
        //и меняем нахвания у кнопок
        if (mod){
            generate1.setText("Paste");
            generate2.setText("Paste");
            generate3.setText("Paste");
        }else {
            generate1.setText("Generate");
            generate2.setText("Generate");
            generate3.setText("Generate");
        }
    }
}
