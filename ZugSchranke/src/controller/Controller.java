package controller;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //region properties
    //region number Property
    private int count = 0;
    private int countCar1 = 0;
    private int countCar2 = 0;
    private int countCar3 = 0;
    private int multiplier = 1;
    //endregion

    //region Timelines
    private Timeline timerCar1;
    private Timeline timerCar2;
    private Timeline timerCar3;
    private Timeline timerTrain;
    //endregion

    //region FXML Properties
    @FXML
    private Slider carroad1;
    @FXML
    private Slider carroad2;
    @FXML
    private Slider carroad3;
    @FXML
    private Slider trainrailway;
    @FXML
    private CheckBox barrier;
    @FXML
    private TextArea messageArea1;
    @FXML
    private TextArea messageArea2;
    @FXML
    private TextArea messageArea3;
    @FXML
    private TextArea messageAreaBarrier;
    @FXML
    private Button trainBtn;
    //endregion
    //endregion

    //region Methods
    public void handleButtonAction() {
        trainBtn.setDisable(true);
        handleTrainTrack();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDisables();
        setupCars();
    }

    private void handleTrainTrack() {
        trainrailway.setDisable(false);
        timerTrain = new Timeline(new KeyFrame(
                Duration.millis(100),
                ae -> onTimerTickTrain()
        ));
        timerTrain.setCycleCount(Animation.INDEFINITE);
        timerTrain.play();
    }

    //region TimerTick Methods
    private void onTimerTickCar1() {
        boolean check = checkBarrierState(carroad1,countCar1);
        if(!check)adjustCars(countCar1,carroad1);
    }
    private void onTimerTickCar2() {
        boolean check = checkBarrierState(carroad2,countCar2);
        if(!check)adjustCars(countCar2,carroad2);
    }
    private void onTimerTickCar3() {
        boolean check = checkBarrierState(carroad3,countCar3);
        if(!check)adjustCars(countCar3,carroad3);
    }

    private void onTimerTickTrain() {
        adjustTrain(count);
        handleBarrier();
        handleTrainStop();
    }
    //endregion

    //region Calculation Methods for carRoad
    private void adjustCars(int adjustValue, Slider carroad){
        if(carroad.getId() == carroad1.getId()){
            if(carroad.getMax() == adjustValue){
                carroad.adjustValue(0);
                countCar1 = 0;
            }
            else{
                carroad.adjustValue(adjustValue);
                countCar1++;
            }
        }
        else if(carroad.getId() == carroad2.getId()){
            if(carroad.getMax() == adjustValue){
                carroad.adjustValue(0);
                countCar2 = 0;
            }
            else{
                carroad.adjustValue(adjustValue);
                countCar2++;
            }
        }
        else if(carroad.getId() == carroad3.getId()){
            if(carroad.getMax() == adjustValue){
                carroad.adjustValue(0);
                countCar3 = 0;
            }
            else{
                carroad.adjustValue(adjustValue);
                countCar3++;
            }
        }

    }
    private boolean checkBarrierState(Slider slider, int count){
        if(barrier.isSelected() == false){
            setMessages(1,getMessageArea(slider));
            setMessages(0,messageAreaBarrier);
            return false;
        }
        else if(barrier.isSelected() == true && count > getClosestLengthToRailway()){
            setMessages(getMessageArea(slider),count,slider);
            setMessages(4,messageAreaBarrier);
            return false;
        }
        else if(barrier.isSelected() == true){
            setMessages(3,getMessageArea(slider));
            setMessages(4,messageAreaBarrier);
            return true;
        }
        else return true;
    }
    private int getClosestLengthToRailway(){
        return (int)(carroad1.getMax()/2-10);
    }
    //endregion

    //region Calculation Methods for trainRailway
    private void adjustTrain(int adjustValue){
        trainrailway.adjustValue(adjustValue);
        count += multiplier;
    }
    private int trainStopped(){
        if(trainrailway.getMax() == count){
            multiplier = -1;
            return 1;
        }
        else if(trainrailway.getMin() == count){
            multiplier = 1;
            return 1;
        }
        else return 0;

    }
    private int fireBarrier(){
        if(getBarrierLength(1) == count){
            return 1;
        }
        else if(getBarrierLength(2) == count){
            return 1;
        }
        return 0;
    }
    private int getBarrierLength(int number){
        if(number == 1){
           return (int)(trainrailway.getMax()/2/2);
        }
        else if(number == 2){
            return (int)((trainrailway.getMax()/2) + (trainrailway.getMax()/2/2));
        }
        else return 0;
    }
    //endregion

    //region beauty Methods
    private void handleBarrier(){
        switch (fireBarrier()){
            case 1:
                barrier.setDisable(false);
                barrier.fire();
                barrier.setDisable(true);
                break;
            case 0:
                break;
        }
    }
    private void handleTrainStop(){
        switch (trainStopped()){
            case 1:
                timerTrain.stop();
                trainrailway.setDisable(true);
                trainBtn.setDisable(false);
                break;
            case 0:
                break;
        }
    }
    private void setDisables(){
        barrier.setDisable(true);
        trainrailway.setDisable(true);
    }

    //region Setup for Cars Methods
    private void setupCars(){
        setupCar1();
        setupCar2();
        setupCar3();
    }
    private void setupCar1(){
        timerCar1 = new Timeline(new KeyFrame(
                Duration.millis(100),
                ae -> onTimerTickCar1()
        ));
        timerCar1.setCycleCount(Animation.INDEFINITE);
        timerCar1.play();
    }
    private void setupCar2(){
        timerCar2 = new Timeline(new KeyFrame(
                Duration.millis(250),
                ae -> onTimerTickCar2()
        ));
        timerCar2.setCycleCount(Animation.INDEFINITE);
        timerCar2.play();
    }
    private void setupCar3(){
        timerCar3 = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> onTimerTickCar3()
        ));
        timerCar3.setCycleCount(Animation.INDEFINITE);
        timerCar3.play();
    }
    //endregion

    //region Message Methods
    private void setMessages(int scenario,TextArea area){
        switch (scenario){
            case 1:
                area.setText("Car is driving");
                break;
            case 3:
                area.setText("Barrier is currently down.\n The Driver has to stop!");
                break;
            case 4:
                area.setText("Barrier is armed");
                break;
                default:
                    area.setText("Barrier is not armed");
                    break;
        }
    }
    private void setMessages(TextArea area,int count,Slider slider){

        area.setText("Barrier is armed, but car is over " + getClosestLengthToRailway() +
                "% done.\n And is currently " +(slider.getMax()- count) +"m away \n to the End of the Street.");
    }
    private TextArea getMessageArea(Slider slider){
        if(slider.getId() == carroad1.getId()){
            return messageArea1;
        }
        else if(slider.getId() == carroad2.getId()){
            return messageArea2;
        }
        else return messageArea3;
    }
    //endregion

    //endregion

    //endregion
}
