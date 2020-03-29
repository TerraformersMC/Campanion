package com.campanion.ropebridge;

import com.campanion.block.CampanionBlocks;
import com.campanion.blockentity.RopeBridgePlanksBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RopeBridge {

    public static final double PLANK_WIDTH = 4/16F;
    public static final double PLANK_LENGTH = 16/16F;
    public static final double MIN_PLANK_PADDING = 2/16F;

    public static final int PLANK_VARIENTS = 6;

    public static final double WEIGHT_OF_PLANK = 0.5/16F; //Per block

    private final Vec3d from;
    private final Vec3d to;

    private final double angle;

    private final double increment;

    //Bezier Curve Points:
    private final double a;
    private final double b;
    private final double c;


    public RopeBridge(BlockPos from, BlockPos to) {
        this.from = new Vec3d(from).add(0.5, 0, 0.5);
        this.to = new Vec3d(to).add(0.5, 0, 0.5);
        this.angle = Math.atan2(this.to.getZ() - this.from.getZ(), this.to.getX() - this.from.getX());

        double length = this.from.distanceTo(this.to);
        double totalPlankSize = PLANK_WIDTH + MIN_PLANK_PADDING;
        int totalPlanks = MathHelper.floor(length / totalPlankSize);

        this.increment = 1D / totalPlanks;
        double s = from.getY();                                                     //beizer start
        double m = (from.getY() + to.getY()) / 2D - WEIGHT_OF_PLANK*totalPlanks;    //beizer middle
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
        this.a = s - 2*m + e;
        this.b = -2*s + 2*m;
        this.c = s;
    }

    public void generateBlocks(World world) {
        double deltaX = this.to.getX() - this.from.getX();
        double deltaZ = this.to.getZ() - this.from.getZ();

        BlockPos previousPostion = BlockPos.ORIGIN;
        BlockPos previousPreviousPosition = BlockPos.ORIGIN;
        List<RopeBridgePlank> previousPlanks = new ArrayList<>();
        for (double d = 0; d <= 1D; d += this.increment) {

            double x = this.from.getX() + deltaX * d;
            double y = this.beizerCurve(d);
            double z = this.from.getZ() + deltaZ * d;

            BlockPos pos = new BlockPos(x, y, z);
            BlockEntity entity = world.getBlockEntity(pos);
            if(!(entity instanceof RopeBridgePlanksBlockEntity)) {
                world.setBlockState(pos, CampanionBlocks.ROPE_BRIDGE_PLANKS.getDefaultState());
                entity = world.getBlockEntity(pos);
            }
            //Should always be true
            if(entity instanceof RopeBridgePlanksBlockEntity) {
                Vec3d vec3d = new Vec3d(MathHelper.floorMod(x, 1D), MathHelper.floorMod(y, 1), MathHelper.floorMod(z, 1));
                double tileAngle = Math.atan(this.beizerCurveGradient(d) / Math.sqrt(deltaX*deltaX + deltaZ*deltaZ));
                RopeBridgePlank plank = new RopeBridgePlank(vec3d, this.angle, tileAngle, world.random.nextInt(PLANK_VARIENTS));
                ((RopeBridgePlanksBlockEntity) entity).getPlanks().add(plank);

                if(!previousPostion.equals(pos)) {
                    for (RopeBridgePlank previousPlank : previousPlanks) {
                        previousPlank.setPrevious(previousPreviousPosition);
                        previousPlank.setNext(pos);
                    }
                    previousPreviousPosition = previousPostion;
                    previousPostion = pos;

                    previousPlanks.clear();
                }
                previousPlanks.add(plank);
            }
        }
    }

    /**
     * Gets the value of the quadratic equation {@code at^2 + bt + c}, derived from the 3 point beizer curve
     * @param t between 0 and 1
     */
    private double beizerCurve(double t) {
        return this.a*t*t + this.b*t + this.c;
    }

    /**
     * Gets the gradient value of the quadratic equation {@code at^2 + bt + c}, derived from the 3 point beizer curve <br>
     * Essentially {@code 2at + b}
     * @param t between 0 and 1
     */
    private double beizerCurveGradient(double t) {
        return 2*this.a*t + this.b;
    }
}
