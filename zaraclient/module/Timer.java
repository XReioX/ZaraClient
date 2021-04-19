package zaraclient.module;

public class Timer {

    public long lastMS = System.currentTimeMillis();

    public void reset(){
        lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long delay, boolean reset){
        if(System.currentTimeMillis() - lastMS > delay){
            if(reset){
                reset();
            }
            return true;
        }else{
            return false;
        }
    }
}
