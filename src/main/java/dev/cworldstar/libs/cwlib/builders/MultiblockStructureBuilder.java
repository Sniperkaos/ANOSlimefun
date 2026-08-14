package dev.cworldstar.libs.cwlib.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockChoice;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockCore;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;

public class MultiblockStructureBuilder {
	
	public static enum Axis {
		X_AXIS,
		Y_AXIS
	}
	
	private Map<Vector, MultiblockChoice> multiblockStructure = new HashMap<Vector, MultiblockChoice>();
	
	/**
	 * You can use this with {@link MultiblockCore#fromStructureBuilder(MultiblockStructureBuilder)}.
	 * A fast way of adding multiblock structure points.
	 * @param id The ID of the given {@link SlimefunItem} block that makes the core of this multiblock.
	 */
	public MultiblockStructureBuilder(String id) {
		multiblockStructure.put(new Vector(0,0,0), new MultiblockChoice(Color.RED, id));
	}
	
	public MultiblockStructureBuilder remove(Vector position) {
		Validate.isTrue(!position.equals(new Vector(0,0,0)), "You cannot set the zero point!");
		if(multiblockStructure.containsKey(position)) {
			multiblockStructure.remove(position);
		}
		return this;
	}
	
	public MultiblockStructureBuilder cube(MultiblockChoice choice, int size) {
		for(int x = -size; x<=size; x++) {
			for(int y=-size;y<=size; y++) {
				for(int z=-size; z<=size; z++) {
					Vector thisVector = new Vector(x, y, z);
					if(multiblockStructure.containsKey(thisVector) || thisVector.isZero()) {
						continue;
					}
					at(thisVector, choice);
				}
			}
		}
		return this;
	}
	
	public MultiblockStructureBuilder column(MultiblockChoice choice, int ySize, int xOffset, int zOffset) {
		for(int i=-ySize; i<=ySize; i++) {
			Vector thisVector = new Vector(xOffset, i, zOffset);
			if(thisVector.isZero()) {
				continue;
			}
			at(thisVector, choice);
		}
		return this;
	}
	
	public MultiblockStructureBuilder column(MultiblockChoice choice, int ySize, int xOffset, int zOffset, int yOffset) {
		for(int i=-ySize; i<=ySize; i++) {
			Vector thisVector = new Vector(xOffset, yOffset + i, zOffset);
			if(thisVector.isZero()) {
				continue;
			}
			at(thisVector, choice);
		}
		return this;
	}
	
    private static boolean edge(Vector vector, int min, int max) {
    	int blockX = vector.getBlockX();
    	int blockZ = vector.getBlockZ();
    	
    	return (blockX >= min && blockX <= max && (blockZ == min || blockZ == max)) || 
    		(blockZ >= min && blockZ <= max && (blockX == min || blockX == max));
    }
	
	public MultiblockStructureBuilder square(String idOrMaterial, int yOffset, int point, Color errorColor) {
		for(int xExtreme=-point; xExtreme<point; xExtreme++) {
			for(int zExtreme=-point; zExtreme<point; zExtreme++) {
				Vector thisVector = new Vector(xExtreme, 0, zExtreme).add(new Vector(0, yOffset, 0));
				if(thisVector.isZero() || !edge(thisVector, -point, point)) {
					continue;
				}
				at(thisVector, idOrMaterial, errorColor);
			}
		}
		return this;
	}
	
	public MultiblockStructureBuilder at(Vector vector, String idOrMaterial, Color errorColor) {
		Validate.isTrue(!vector.equals(new Vector(0,0,0)), "You cannot set the zero point!");
		Validate.notNull(vector, "offsetInObjectSpace must not be null!");
		Validate.notNull(idOrMaterial, "ID or Material must not be null!");
		this.multiblockStructure.put(vector, new MultiblockChoice(errorColor, idOrMaterial));
		return this;
	}
	
	public MultiblockStructureBuilder at(Vector vector, MultiblockChoice idOrMaterial) {
		Validate.isTrue(!vector.equals(new Vector(0,0,0)), "You cannot set the zero point!");
		Validate.notNull(vector, "offsetInObjectSpace must not be null!");
		Validate.notNull(idOrMaterial, "ID or Material must not be null!");
		this.multiblockStructure.put(vector,idOrMaterial);
		return this;
	}
	
	public MultiblockStructureBuilder at(MultiblockChoice idOrMaterial, Vector... vectors) {
		for(Vector vector : vectors) {
			Validate.isTrue(!vector.equals(new Vector(0,0,0)), "You cannot set the zero point!");
			Validate.notNull(vector, "offsetInObjectSpace must not be null!");
			Validate.notNull(idOrMaterial, "ID or Material must not be null!");
			this.multiblockStructure.put(vector,idOrMaterial);
		}
		return this;
	}
	
	public Map<Vector, MultiblockChoice> structure() {
		return multiblockStructure;
	}

	public MultiblockStructureBuilder square(MultiblockChoice multiblockChoice, int yOffset, int point) {
		for(int xExtreme=-point; xExtreme<=point; xExtreme++) {
			for(int zExtreme=-point; zExtreme<=point; zExtreme++) {
				Vector thisVector = new Vector(xExtreme, 0, zExtreme).add(new Vector(0, yOffset, 0));
				if(thisVector.isZero() || !edge(thisVector, -point, point)) {
					continue;
				}
				at(thisVector, multiblockChoice);
			}
		}
		return this;
	}
	
	public MultiblockStructureBuilder square(MultiblockChoice multiblockChoice, int yOffset, int xOffset, int zOffset, int point) {
		for(int xExtreme=-point; xExtreme<=point; xExtreme++) {
			for(int zExtreme=-point; zExtreme<=point; zExtreme++) {
				Vector thisVector = new Vector(xExtreme, 0, zExtreme).add(new Vector(xOffset, yOffset, zOffset));
				if(thisVector.isZero() || !edge(thisVector, -point, point)) {
					continue;
				}
				at(thisVector, multiblockChoice);
			}
		}
		return this;
	}

	public static List<Vector> getVectorsInSquare(Block core, int yOffset, int point) {
		List<Vector> vectors = new ArrayList<Vector>();
		for(int xExtreme=-point; xExtreme<=point; xExtreme++) {
			for(int zExtreme=-point; zExtreme<=point; zExtreme++) {
				Vector thisVector = new Vector(xExtreme, 0, zExtreme).add(new Vector(0, yOffset, 0));
				if(thisVector.isZero() || !edge(thisVector, -point, point)) {
					continue;
				}
				vectors.add(thisVector);
			}
		}
		return vectors;
	}

	public MultiblockStructureBuilder solidSquare(MultiblockChoice multiblockChoice, int point, int yOffset) {
		for(int xExtreme=-point; xExtreme<=point; xExtreme++) {
			for(int zExtreme=-point; zExtreme<=point; zExtreme++) {
				Vector thisVector = new Vector(xExtreme, 0, zExtreme).add(new Vector(0, yOffset, 0));
				if(thisVector.isZero()) {
					continue;
				}
				at(thisVector, multiblockChoice);
			}
		}
		return this;
	}

	public MultiblockStructureBuilder line(MultiblockChoice multiblockChoice, Vector from, Vector to) {
		ArrayList<Vector> vectors = null;
		if(from.getBlockY() > 0 && (from.getBlockX() == 0 && from.getBlockZ() == 0)) {
			vectors = lineY(from, to);
		} else {
			vectors = lineXZ(from, to);
		}
		for(Vector vector : vectors) {
			if(!vector.isZero()) {
				at(vector, multiblockChoice);
			}
		}
		return this;
	}
	
	
	public static ArrayList<Vector> lineY(Vector from, Vector to) {
		ArrayList<Vector> vectors = new ArrayList<Vector>();
		for(double yExtreme = -from.getY(); yExtreme <= to.getY(); yExtreme++) {
			vectors.add(new Vector(0, yExtreme, 0));
		}
		return vectors;
	}
	
	public static ArrayList<Vector> lineXZ(Vector from, Vector to) {
		ArrayList<Vector> vectors = new ArrayList<Vector>();
		double signumX = Math.signum(from.getX());
		if(signumX >= 1.0) {
			for(double fX = from.getX(); fX>= to.getX(); fX--) {
				double signumZ = Math.signum(from.getZ());
				if(signumZ <= -1.0) {
					for(double fZ = from.getZ(); fZ<=to.getZ(); fZ++) {
						vectors.add(new Vector(fX, 0, fZ));
					}	
				} else {
					for(double fZ = from.getZ(); fZ>=to.getZ(); fZ--) {
						vectors.add(new Vector(fX, 0, fZ));
					}	
				}
			}
		} else {
			for(double fX = from.getX(); fX<= to.getX(); fX++) {
				double signumZ = Math.signum(from.getZ());
				if(signumZ <= -1.0) {
					for(double fZ = from.getZ(); fZ<=to.getZ(); fZ++) {
						vectors.add(new Vector(fX, 0, fZ));
					}	
				} else {
					for(double fZ = from.getZ(); fZ>=to.getZ(); fZ--) {
						vectors.add(new Vector(fX, 0, fZ));
					}	
				}
			}
		}
		return vectors;
	}
}
