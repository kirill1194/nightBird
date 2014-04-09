package Classes;
/*
 * ОПИСАНИЕ АЛГОРИТМА!
 * если встаем на жизнь - копим 5 жизней и продолжаем движение
 * ищем путь и идем к жизни, если жизней становится <= 3
 */

/*
 * ЗАДАЧИ АЛГОРИТМА
 * 1)При составлении пути до жизни учитывать, 
 * 		что клетка с капканам = 2 ходам, тк придется на 1 ход больше стоять на жизни
 */
import java.util.ArrayList;
import java.util.HashMap;

import Enums.Action;
import Enums.Direction;

public class Mause {
	enum Task {
		goToFinish,
		goToLife,
		increaseLife,
		goTheEndOfWay;
	}
//Будем ходить по правилу правой руки
	HashMap<Point, Action> map;
	private int lifeCount;
	//вектор направления движения
	private Point moveDirection;
	//Текущее цель мыши
	private Task task;
	//Направление прошлого хода
	private Direction lastDirection;
	//Точка, в которой мы были перед тем, как пошли обратно за жизнями
	private Point endWay;
	//Текущее положение мыши
	private Point thisPoint;
	//Точка, котротой была мыш перед попыткой перейни на новую точку
	private Point nextPoint;
	//Путь до ближайшей жизни
	private ArrayList <Point> wayToLife;
	private int indexInWayToLife;
	private int needLifes;
	public Mause() {
		lifeCount = 3;
		//Хочу, что бы первым движением мышь шла вниз, тк мы повернем вектор налево, то указываем вектор,
		//		направленный налево
		moveDirection = new Point (-1, 0);
		thisPoint = new Point (0, 0);
		nextPoint = null;
		map = new HashMap<>();
		wayToLife = null;
		task = Task.goToFinish;
	}
	//Поворот вектора движения налево
	private Point turnLeft(Point pt) {
		return new Point(pt.getY(), -1*pt.getX());
	}
	//Поворот вектора движения направо
	private Point turnRight(Point pt) {
		return new Point(-1*pt.getY(), pt.getX());
	}
	
	//Конвертация вектора движения в дирекшена
	private Direction pointToDirection(Point p) {
		if (p.equals(new Point(0, 0)))
			return Direction.None;
		if (p.equals(new Point(1, 0)))
			return Direction.Right;
		if (p.equals(new Point(0, 1)))
			return Direction.Down;
		if (p.equals(new Point(-1, 0)))
			return Direction.Left;
		if (p.equals(new Point(0, -1)))
			return Direction.Up;
		throw new RuntimeException();		
	}
	//Конвертация дирекшена в вектор движения
	private Point directionToPoint(Direction d)
	{
		switch (d) {
		case Down:
			return new Point (0, 1);
		case Left:
			return new Point(-1, 0);
		case Up:
			return new Point(0, -1);
		case Right:
			return new Point(1, 0);
		case None:
			return new Point(0, 0);
		}
		return null;
	}
	
	private boolean buildWayToLife()
	{
		indexInWayToLife = 0;
		//Вернет true, если при пути до жизни игрок не умрет, иначе False
		//Записать в wayToLife путь до ближайщей жизни
		//wayToLife[0] - точка в которой мы были перед тем, как начать движение к жизни
		//wayToLife[n] - жизнь
		//needLifes = 4+количество капканов на пути
		return true;
	};

	private Direction setNextNone()
	{
		nextPoint = thisPoint;
		lastDirection = Direction.None;
		return Direction.None;
	}
	private Direction setNextRight()
	{
		nextPoint = thisPoint.MoveRigth();
		lastDirection = Direction.Right;
		return Direction.Right;
	}
	private Direction setNextDown()
	{
		nextPoint = thisPoint.MoveDown();
		lastDirection = Direction.Down;
		return Direction.Down;
	}
	private Direction setNextLeft()
	{
		nextPoint = thisPoint.MoveLeft();	
		lastDirection = Direction.Left;
		return Direction.Left;
	}
	private Direction setNextUp()
	{
		nextPoint = thisPoint.MoveUp();	
		lastDirection = Direction.Up;
		return Direction.Up;
	}
	private Direction setNext(Direction dir) {
		Point dirPoint = directionToPoint(dir);
		nextPoint = new Point(thisPoint.getX()+dirPoint.getX(), thisPoint.getY()+dirPoint.getY());
		moveDirection= dirPoint;
		return dir;
	}
	
	
	public Direction move(Action action) {
		//по дефолту nextPoint=null, если так, значит это первый ход => вернем None и узнаем на какой мы стоим клетке
		if (nextPoint == null)
			return setNextNone();
		if (action != Action.Fail)
			thisPoint = nextPoint;
		
		map.put(nextPoint, action);
		
		switch (map.get(thisPoint)) {
			case Dead:
				lifeCount--;
				break;
			case Life:
				lifeCount++;
				break;
			default:
				break;
		}
		
		//если наступаем на капкан, то смотрим сколько жизней, смотрим есть ли известаня
		//жизнь и достижима ли она, и принимаем решение: идти за жизнью или продолжать поиск финиша
		if (task == Task.goToFinish && map.get(thisPoint)==Action.Dead ) {
			if ( (lifeCount<=3) && (map.containsValue(Action.Life)) )
				if (buildWayToLife()) {
					endWay = thisPoint;
					task = Task.goToLife;
				}
		}
		//Если мы находимся в состоянии движения к жизни
		if (task == Task.goToLife) {
			//если мы в конце пути (стоим на жизни)
			if (indexInWayToLife == wayToLife.size()-1) {
				//копим жизни
				if (lifeCount <= needLifes)
					return Direction.None;
				else {
					//переходим к задаче: вернуться в точку, в которой мы перешли к поиску жизни
					task = Task.goTheEndOfWay;
				}
			}
			//Если мы еще не дошли для искомой жизни
			else {
				//Вычесляем новое направление движения
				Point thisP = wayToLife.get(indexInWayToLife);
				Point nextP = wayToLife.get(indexInWayToLife+1);
				Point nextEnter = new Point(nextP.getX() - thisP.getX(), nextP.getY() - thisP.getY());
				//Запоминаем, что пршли новую точку в пути
				indexInWayToLife++;
				return setNext(pointToDirection(nextEnter));
			}
		}
		
		//Если мы находимся в состоянии движения к точке, в которой мы перешли к поиску жизни
		if (task == Task.goTheEndOfWay) {
			//Если мы уже вернулись к этой точке
			if (indexInWayToLife == 0)
				//Переходим в состояния движения к финишу
				task = Task.goToFinish;
			else {
				//Иначе вычисляем направление нового шага
				Point thisP = wayToLife.get(indexInWayToLife);
				Point nextP = wayToLife.get(indexInWayToLife-1);
				Point nextEnter = new Point(nextP.getX() - thisP.getX(), nextP.getY() - thisP.getY());
				//Не забываем отметить, что мы прошли точку в пути
				indexInWayToLife--;
				return setNext(pointToDirection(nextEnter));
			}
		}
		
		//Если мы находимся в состоянии движения к финишу
		if (task == Task.goToFinish) {
			//Если мы наступили на жизнь
			if ( map.get(thisPoint)==Action.Life )
				//Если у нас мало жизней (<=4)
				if (lifeCount <= 4)
					//то немного постоим
					return Direction.None;
			//Если мы на прошлом шаге ударились в стену
			if (action == Action.Fail) {
				//то поворачиваем направо
				Point nextMoveDirection = turnRight(moveDirection);
				return setNext(pointToDirection(nextMoveDirection));
			}
			//Еслимы не ударились об стену
			else{
				//то повернем налево
				Point nextMoveDirection = turnLeft(moveDirection);
				return setNext(pointToDirection(nextMoveDirection));
			}
		}

		throw new RuntimeException("ВСЕ ПЛОХО!");
	}
}
		
