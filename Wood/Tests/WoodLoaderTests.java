package Tests;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Before;
import org.junit.Test;

import Classes.Point;
import Classes.WoodLoader;
import Enums.Action;
import Enums.Direction;
import Exceptions.EmptyFileException;
import Exceptions.InvalidFileException;
import Exceptions.OccupiedLocationException;
import Exceptions.UnexceptableNameException;
import Interfaces.WoodInterface;
import Interfaces.WoodLoaderInterface;



public class WoodLoaderTests{
	private WoodInterface myWood;
	@Before
	public void makeWood() {
		String[] masOfStr = new String [4];
		masOfStr[0] = "1111\n";
		masOfStr[1] = "1001\n";
		masOfStr[2] = "1LK1\n";
		masOfStr[3] = "1111\n";
		StringBuilder strBuild = new StringBuilder();
		for (int i=0; i<masOfStr.length; i++)
			strBuild.append(masOfStr[i]);
		String str = strBuild.toString();
		byte[] buf = str.getBytes();
		ByteArrayInputStream stream = new ByteArrayInputStream(buf);
		WoodLoaderInterface loader = new WoodLoader();
		try {
			myWood = loader.Load(stream);
		} catch (EmptyFileException e) {
			fail("EmptyFileException");
			e.printStackTrace();
		} catch (InvalidFileException e) {
			fail("InvalidFileException");
			e.printStackTrace();
		}
	}

	@Test(expected = EmptyFileException.class)
	public void EmptyFileExceptionTest() throws EmptyFileException, InvalidFileException {
		String str = "";
		byte[] mas = str.getBytes();
		ByteArrayInputStream stream = new ByteArrayInputStream(mas);
		WoodLoaderInterface loader = new WoodLoader();
		loader.Load(stream);
	}
	
	
	@Test(expected = InvalidFileException.class)
	public void InvalidFileExceptionTest() throws EmptyFileException, InvalidFileException {
		String str = "111\n101\n1011";
		byte[] mas = str.getBytes();
		ByteArrayInputStream stream = new ByteArrayInputStream(mas);
		WoodLoaderInterface loader = new WoodLoader();
		loader.Load(stream);
	}
	
	
	@Test
	public void testLoad() throws EmptyFileException, InvalidFileException {
		try {
			myWood.createWoodman("Player1", new Point(1, 1));
			assertEquals(Action.Life, myWood.move("Player1", Direction.Down));
			assertEquals(Action.Dead, myWood.move("Player1", Direction.Right));
			assertEquals(Action.Ok, myWood.move("Player1", Direction.Up)); //1 жизнь
			assertEquals(Action.Ok, myWood.move("Player1", Direction.Left));
			
		} catch (UnexceptableNameException e) {
			fail("UnexceptableNameException");
			e.printStackTrace();
		} catch (OccupiedLocationException e) {
			fail("OccupiedLocationException");
			e.printStackTrace();
		}
	}

}
