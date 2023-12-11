package a02a.e2;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import a02a.e2.Pair;

public class LogicImpl implements Logic{
    private int size;
    private int nClicks;
    private List<Pair<Integer, Integer>> clickedList;
    private GUI gui;
    private Optional<Pair<Integer, Integer>> lastClicked;

    public LogicImpl (int size, GUI gui){
        this.size = size;
        nClicks = 0;
        this.clickedList = new LinkedList<>();
        this.gui = gui;
    }

    @Override
    public Optional<Pair<Integer, Integer>> newClick() {
        if ( nClicks > size*size){
            return Optional.empty();
        }else{
            Optional<Pair<Integer, Integer>> newCoord = newCoords();
            nClicks++;
            this.lastClicked = newCoord;
            if(!newCoord.isEmpty()){
                this.clickedList.add(newCoord.get());
            }
            return newCoord;
        }
    }

    private Optional<Pair<Integer, Integer>> newCoords(){
        if(nClicks == 0){
            Random rn = new Random();
            return Optional.of(new Pair<>(rn.nextInt(0, size), rn.nextInt(0, size)));
        }
        if(nClicks == 1){
            if(lastClicked.get().getY() == 0){
                return Optional.empty();
            }else{
                return Optional.of(new Pair<>(lastClicked.get().getX(), lastClicked.get().getY() - 1));
            }
        }
        return getAdjacent(lastClicked.get());
    }

    private Optional<Pair<Integer, Integer>> getAdjacent (Pair<Integer, Integer> coord){
        if(isInGrid(coord.getX() - 1, coord.getY()))return Optional.of(new Pair<>(coord.getX() - 1, coord.getY())); 
        if(isInGrid(coord.getX() + 1, coord.getY()))return Optional.of(new Pair<>(coord.getX() + 1, coord.getY()));
        if(isInGrid(coord.getX(), coord.getY() - 1))return Optional.of(new Pair<>(coord.getX(), coord.getY() - 1)); 
        if(isInGrid(coord.getX(), coord.getY() + 1))return Optional.of(new Pair<>(coord.getX(), coord.getY() + 1));
        return Optional.empty();
    }

    private boolean isInGrid (int x, int y){
        Pair<Integer, Integer> coord = new Pair<Integer,Integer>(x, y);
        if(coord.getX() >= 0 && coord.getX() < size && coord.getY() >= 0 && coord.getY() < size && !this.clickedList.contains(coord))return true;
        return false;
    }

    
}
