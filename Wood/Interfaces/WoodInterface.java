package Interfaces;


import Exceptions.OccupiedLocationException;
import Exceptions.UnexceptableNameException;
import Classes.Point;
import Enums.Action;
import Enums.Direction;



public interface WoodInterface {
	/**
	 * Создает нового лесного жителя.
	 * 
	 * @param name имя лесного жителя
	 * @param start место появления
	 */
	void createWoodman(String name, Point start) throws UnexceptableNameException, OccupiedLocationException;
	
	/**
	 * Перемещает лесного жителя.
	 * 
	 * @param name имя лесного жителя
	 * @param direction направление перемещения
	 * @return результат перемещения
	 */
	Action move(String name, Direction direction) throws UnexceptableNameException, OccupiedLocationException;
}
