package Server;

import java.util.Objects;

public class Localizacao{
    private int x;
    private int y;

    public Localizacao(){
        this.x = 0;
        this.y = 0;
    }

    public Localizacao(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Localizacao(Localizacao local){
        this.x = local.getX();
        this.y = local.getY();
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localizacao that = (Localizacao) o;
        return x == that.x && y == that.y;
    }

    public Localizacao clone(){
        return new Localizacao(this);
    }


}