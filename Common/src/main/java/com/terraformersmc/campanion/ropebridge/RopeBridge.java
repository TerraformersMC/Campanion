package com.terraformersmc.campanion.ropebridge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

public class RopeBridge {

	public static final double PLANK_WIDTH = 4 / 16F;
	public static final double PLANK_LENGTH = 15.9 / 16F;
	public static final double MIN_PLANK_PADDING = 2 / 16F;
	public static final int PLANKS_PER_ROPE = 3;
	public static final int PLANK_VARIANT_TEXTURES = 2;
	public static final int PLANKS_PER_ITEM = 5;

	public static final int ROPE_WIDTH = 1;
	public static final float UNDER_ROPE_DIST_FROM_EDGE = 2F;
	public static final float ROPE_LENGTH = 14.5F;
	public static final int KNOT_SIZE = 1;
	public static final int BLOCKS_PER_ROPE = 5;

	public static final int STOPPER_WIDTH = 4;
	public static final int STOPPER_HEIGHT = 16;

	public static final double LIMITING_ANGLE = Math.PI / 4D;
	public static final double LIMITING_XZ_DIST = 250;

	public static final double WEIGHT_OF_PLANK = 0.5 / 16F; //Per block

	private final Vec3 from;
	private final Vec3 to;

	private final double angle;

	private final int totalPlanks;

	//Bezier Curve Points:
	private final double a;
	private final double b;
	private final double c;

	public RopeBridge(BlockPos from, BlockPos to) {
		this.from = Vec3.atBottomCenterOf(from);
		this.to = Vec3.atBottomCenterOf(to);
		this.angle = Math.atan2(this.to.z() - this.from.z(), this.to.x() - this.from.x());

		double length = this.from.distanceTo(this.to);
		double totalPlankSize = PLANK_WIDTH + MIN_PLANK_PADDING;
		this.totalPlanks = Mth.floor(length / totalPlankSize);

		double s = from.getY();                                                     //beizer start
		double m = (from.getY() + to.getY()) / 2D - WEIGHT_OF_PLANK * totalPlanks;    //beizer middle
		double e = to.getY();                                                       //beizer end


		//The definition of a 3 point bezier curve is as follows:
		//(1-t)^2s + 2(1-t)mt + et^2
		//Where s, m and e are 3 different points on the curve.
		//For optimization and for the differentiation to work, I want to get the equation in the form of at^2 + bt + c
		// = (1-2t+t^2)s + (2t-2t^2)m + et^2
		// = s - 2st + st^2 + 2mt - 2mt^2 + et^2
		// = (s - 2m + e)t^2 + (-2s + 2m)t + (s)
		// = at^2 + bt + c, where:
		//                      a = s - 2m + e
		//                      b = -2s + 2m
		//                      c = s
		this.a = s - 2 * m + e;
		this.b = -2 * s + 2 * m;
		this.c = s;
	}

	public Optional<Component> getFailureReason() {
		double deltaX = this.to.x() - this.from.x();
		double deltaZ = this.to.z() - this.from.z();
		double xzDist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

		double theta = Math.abs(Math.atan((this.to.y() - this.from.y()) / xzDist));
		if (this.to.equals(this.from)) {
			return Optional.of(Component.translatable("message.campanion.rope_bridge.same_position"));
		}
		if (theta > LIMITING_ANGLE) {
			return Optional.of(Component.translatable("message.campanion.rope_bridge.angle", Math.round(theta * 1800D / Math.PI) / 10D, Math.round(LIMITING_ANGLE * 1800D / Math.PI) / 10F));
		}
		if (xzDist > LIMITING_XZ_DIST) {
			return Optional.of(Component.translatable("message.campanion.rope_bridge.length", Math.round(xzDist * 10D) / 10D, Math.round(LIMITING_XZ_DIST * 10D) / 10D));
		}
		return Optional.empty();
	}

	public List<Pair<BlockPos, List<RopeBridgePlank>>> generateBlocks(Level world) {
		double hl = RopeBridge.PLANK_LENGTH / 2D;
		BlockPos[] positionOrder = new BlockPos[this.totalPlanks + 1];
		Map<BlockPos, List<Pair<Float, RopeBridgePlank>>> map = new HashMap<>();
		this.generateBlocks(world, positionOrder, 0, 0, map);
		this.generateBlocks(world, positionOrder, hl * Math.sin(this.angle), -hl * Math.cos(this.angle), map);
		this.generateBlocks(world, positionOrder, -hl * Math.sin(this.angle), hl * Math.cos(this.angle), map);

		//Sorting
		List<Triple<BlockPos, Float, RopeBridgePlank>> fulList = new LinkedList<>();
		map.forEach((pos, pairs) -> {
			for (Pair<Float, RopeBridgePlank> pair : pairs) {
				fulList.add(Triple.of(pos, pair.getLeft(), pair.getRight()));
			}
		});
		fulList.sort(Comparator.comparing(Triple::getMiddle));

		List<Pair<BlockPos, List<RopeBridgePlank>>> out = new LinkedList<>();
		for (Triple<BlockPos, Float, RopeBridgePlank> triple : fulList) {
			Optional<List<RopeBridgePlank>> first = out.stream().filter(blockPosListPair -> blockPosListPair.getLeft().equals(triple.getLeft())).map(Pair::getRight).findFirst();
			List<RopeBridgePlank> list;
			if (first.isPresent()) {
				list = first.get();
			} else {
				list = new LinkedList<>();
				out.add(Pair.of(triple.getLeft(), list));
			}
			list.add(triple.getRight());
		}
		return out;
	}

	private void generateBlocks(Level world, BlockPos[] positionOrder, double xOff, double zOff,
								Map<BlockPos, List<Pair<Float, RopeBridgePlank>>> map
	) {
		boolean master = xOff == 0 && zOff == 0;
		double deltaX = this.to.x() - this.from.x();
		double deltaZ = this.to.z() - this.from.z();

		BlockPos fromPos = BlockPos.containing(this.from);
		BlockPos toPos = BlockPos.containing(this.to);

		// BlockPos fromPos = new BlockPos(this.from);
		// BlockPos toPos = new BlockPos(this.to);

		int index = 0;
		int offset = world.random.nextInt(PLANKS_PER_ROPE);

		Vec3 calculatedPosition = new Vec3(xOff + this.from.x(), this.from.y(), zOff + this.from.z());
		for (int i = 0; i <= this.totalPlanks; i++) {
			double d = (double) i / this.totalPlanks;
			double nextD = Math.min((double) (i + 1) / this.totalPlanks, 1D);

			BlockPos pos = BlockPos.containing(calculatedPosition);
			//BlockPos pos = new BlockPos(calculatedPosition);
			float ropesSubtract = 0;
			boolean flat = pos.equals(fromPos) || pos.equals(toPos);
			if (pos.equals(fromPos.below()) || pos.equals(toPos.below())) {
				pos = pos.above();
				ropesSubtract = (float) (pos.getY() - calculatedPosition.y);
				calculatedPosition = new Vec3(calculatedPosition.x, pos.getY(), calculatedPosition.z);
				flat = true;
			}
			Vec3 newPos = new Vec3(xOff + this.from.x() + deltaX * nextD, this.beizerCurve(nextD), zOff + this.from.z() + deltaZ * nextD);

			if ((master || !positionOrder[index].equals(pos))) {
				Vec3 vec3d = new Vec3(floorMod(calculatedPosition.x, 1D) - xOff, floorMod(calculatedPosition.y + 0.001, 1), floorMod(calculatedPosition.z, 1) - zOff);
				double tiltAngle = Math.atan(this.beizerCurveGradient(d) / Math.sqrt(deltaX * deltaX + deltaZ * deltaZ));

				if (Double.isNaN(tiltAngle)) {
					tiltAngle = 0;
				}

				map.computeIfAbsent(pos, p -> new LinkedList<>()).add(
					Pair.of(index + (master ? 0F : 0.5F),
						new RopeBridgePlank(
							fromPos, toPos, vec3d, this.angle, tiltAngle, (float) newPos.subtract(calculatedPosition).length(),
							ropesSubtract, world.random.nextInt(128), flat, master,
							(offset + index) % PLANKS_PER_ROPE == 0, i == 0 || i == this.totalPlanks
						)
					)
				);

			}
			if (master) {
				positionOrder[index] = pos;
			}
			index++;
			calculatedPosition = newPos;
		}
	}

	/**
	 * Gets the value of the quadratic equation {@code at^2 + bt + c}, derived from the 3 point beizer curve
	 *
	 * @param t between 0 and 1
	 */
	private double beizerCurve(double t) {
		return this.a * t * t + this.b * t + this.c;
	}

	/**
	 * Gets the gradient value of the quadratic equation {@code at^2 + bt + c}, derived from the 3 point beizer curve <br>
	 * Essentially {@code 2at + b}
	 *
	 * @param t between 0 and 1
	 */
	private double beizerCurveGradient(double t) {
		return 2 * this.a * t + this.b;
	}

	public static double floorMod(double d, double e) {
		return (d % e + e) % e;
	}
}
