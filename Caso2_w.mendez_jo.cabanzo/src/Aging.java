import java.util.HashMap;

public class Aging extends Thread {
    private HashMap<Integer, Integer> memoriaFisica;
    protected boolean isAlive;
    private Class<Caso2> main;

    public Aging(HashMap<Integer, Integer> memoriaFisica2, boolean isAlive) {
        this.memoriaFisica = memoriaFisica2;
        this.isAlive = isAlive;
    }

    @Override
    public void run() {
        while (isAlive) {
            synchronized (memoriaFisica) {
                for (Integer key : memoriaFisica.keySet()) {
                    // System.out.println(key + " - " + Integer.toBinaryString(memoriaFisica.get(key)));
                    memoriaFisica.replace(key, memoriaFisica.get(key) >>> 1);
                    // memoriaFisica.put(key, memoriaFisica.get(key) >>> 1);
                    // System.out.println(key + " - " + Integer.toBinaryString(memoriaFisica.get(key)));
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
