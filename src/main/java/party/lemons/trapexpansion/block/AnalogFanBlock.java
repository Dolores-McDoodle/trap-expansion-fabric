package party.lemons.trapexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AnalogFanBlock extends FanBlock {
	public static final IntProperty POWER = Properties.POWER;
	
	public AnalogFanBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateFactory.getDefaultState().with(POWER, 0).with(POWERED, false).with(FACING, Direction.SOUTH));
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos pos2, boolean boolean_1) {
		boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
		
		if (powered) {
			int power = world.getReceivedRedstonePower(pos);
			int upPower = world.getReceivedRedstonePower(pos.up());
			if(upPower > power) power = upPower;
			world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
			world.setBlockState(pos, state.with(POWER, power).with(POWERED, true));
		} else {
			if (state.get(POWER) > 0 || state.get(POWERED)) {
				world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
				world.setBlockState(pos, state.with(POWER, 0).with(POWERED, false));
			}
		}
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean bool) {
		if (world.isReceivingRedstonePower(pos)) {
			int power = world.getReceivedRedstonePower(pos);
			int upPower = world.getReceivedRedstonePower(pos.up());
			if(upPower > power) power = upPower;
			world.getBlockTickScheduler().schedule(pos, this, this.getTickRate(world));
			world.setBlockState(pos, state.with(POWER, power).with(POWERED, true));
		}
	}
	
	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> st) {
		st.add(FACING).add(POWER).add(POWERED);
	}
	
	@Override
	public double getFanRange(BlockState state) {
		int power = state.get(POWER);
		return (((double)power)/2)+1;
	}
}
