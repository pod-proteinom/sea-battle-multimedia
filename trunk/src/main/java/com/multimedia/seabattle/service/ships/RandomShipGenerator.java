package com.multimedia.seabattle.service.ships;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.types.GameShipType;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.collisions.IShipCollision;


public class RandomShipGenerator implements IShipGenerator{
	private Set<GameShipType> types = EnumSet.<GameShipType>of(GameShipType.CLASSIC);

	/**
	 * quantity of time the battlefield will be re-generated on error
	 */
	private int battlefield_try_quantity = 10;

	/**
	 * quantity of time the ship will be re-generated on error
	 */
	private int ship_try_quantity = 10;

	@Override
	public Map<Coordinates, ShipType> generateShips(IGameShips game_ships,
			IShipCollision collision_handler, Game game) {
		RandomGeneratorDelegate delegate = new RandomGeneratorDelegate(game_ships,
				collision_handler, game, battlefield_try_quantity, ship_try_quantity);
		return delegate.generate();
	}

	//TODO: add some more types allowed
	@Override
	public boolean supports(Game game) {
		return game.getType()==GameShipType.CLASSIC;
	}

	@Override
	public Set<GameShipType> getSupportedGames() {
		return types;
	}

}
