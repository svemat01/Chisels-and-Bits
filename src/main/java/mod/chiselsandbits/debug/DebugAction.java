package mod.chiselsandbits.debug;

import mod.chiselsandbits.api.APIExceptions.CannotBeChiseled;
import mod.chiselsandbits.api.APIExceptions.InvalidBitItem;
import mod.chiselsandbits.api.APIExceptions.SpaceOccupied;
import mod.chiselsandbits.api.IBitAccess;
import mod.chiselsandbits.api.IBitBrush;
import mod.chiselsandbits.api.IBitLocation;
import mod.chiselsandbits.api.IBitVisitor;
import mod.chiselsandbits.api.IChiselAndBitsAPI;
import mod.chiselsandbits.api.ItemType;
import mod.chiselsandbits.core.ChiselsAndBits;
import mod.chiselsandbits.core.Log;
import mod.chiselsandbits.items.ItemChiseledBit;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class DebugAction
{

	public static IChiselAndBitsAPI api;

	private static void apiAssert(
			final String name,
			final EntityPlayer player,
			final boolean must_be_true )
	{
		if ( must_be_true != true )
		{
			player.addChatMessage( new ChatComponentText( name + " = false" ) );
		}
	}

	public abstract void run(
			final World w,
			final BlockPos pos,
			final EnumFacing side,
			final float hitX,
			final float hitY,
			final float hitZ,
			EntityPlayer player );

	static class ItemTests extends DebugAction
	{

		@Override
		public void run(
				final World w,
				final BlockPos pos,
				final EnumFacing side,
				final float hitX,
				final float hitY,
				final float hitZ,
				final EntityPlayer player )
		{
			final IBitAccess access = api.createBitItem( null );

			apiAssert( "BIT_BAG", player, api.getItemType( new ItemStack( ChiselsAndBits.getItems().itemBitBag ) ) == ItemType.BIT_BAG );
			apiAssert( "CHISEL", player, api.getItemType( new ItemStack( ChiselsAndBits.getItems().itemChiselDiamond ) ) == ItemType.CHISEL );
			apiAssert( "MIRROR_DESIGN 1", player, api.getItemType( new ItemStack( ChiselsAndBits.getItems().itemMirrorprint ) ) == ItemType.MIRROR_DESIGN );
			apiAssert( "NEGATIVE_DESIGN 1", player, api.getItemType( new ItemStack( ChiselsAndBits.getItems().itemNegativeprint ) ) == ItemType.NEGATIVE_DESIGN );
			apiAssert( "POSITIVE_DESIGN 1", player, api.getItemType( new ItemStack( ChiselsAndBits.getItems().itemPositiveprint ) ) == ItemType.POSITIVE_DESIGN );
			apiAssert( "WRENCH", player, api.getItemType( new ItemStack( ChiselsAndBits.getItems().itemWrench ) ) == ItemType.WRENCH );
			apiAssert( "CHISLED_BIT-cobblestone", player, api.getItemType( ItemChiseledBit.createStack( Block.getStateId( Blocks.cobblestone.getDefaultState() ), 1, true ) ) == ItemType.CHISLED_BIT );
			apiAssert( "CHISLED_BLOCK", player, api.getItemType( access.getBitsAsItem( null, ItemType.CHISLED_BLOCK ) ) == null );
			apiAssert( "MIRROR_DESIGN 2", player, api.getItemType( access.getBitsAsItem( null, ItemType.MIRROR_DESIGN ) ) == null );
			apiAssert( "NEGATIVE_DESIGN 2", player, api.getItemType( access.getBitsAsItem( null, ItemType.NEGATIVE_DESIGN ) ) == null );
			apiAssert( "POSITIVE_DESIGN 2", player, api.getItemType( access.getBitsAsItem( null, ItemType.POSITIVE_DESIGN ) ) == null );

			try
			{
				final IBitBrush brush = api.createBrush( api.getBitItem( Blocks.cobblestone.getDefaultState() ) );
				access.setBitAt( 0, 0, 0, brush );
			}
			catch ( final InvalidBitItem e )
			{
				apiAssert( "createBrush/getBitItem", player, false );
			}
			catch ( final SpaceOccupied e )
			{
				apiAssert( "setBitAt", player, false );
			}

			apiAssert( "CHISLED_BLOCK 2", player, api.getItemType( access.getBitsAsItem( null, ItemType.CHISLED_BLOCK ) ) == ItemType.CHISLED_BLOCK );
			apiAssert( "MIRROR_DESIGN 3", player, api.getItemType( access.getBitsAsItem( null, ItemType.MIRROR_DESIGN ) ) == ItemType.MIRROR_DESIGN );
			apiAssert( "NEGATIVE_DESIGN 3", player, api.getItemType( access.getBitsAsItem( null, ItemType.NEGATIVE_DESIGN ) ) == ItemType.NEGATIVE_DESIGN );
			apiAssert( "POSITIVE_DESIGN 3", player, api.getItemType( access.getBitsAsItem( null, ItemType.POSITIVE_DESIGN ) ) == ItemType.POSITIVE_DESIGN );
			apiAssert( "WRENCH", player, api.getItemType( access.getBitsAsItem( null, ItemType.WRENCH ) ) == null );
		}

	};

	static class canBeChiseled extends DebugAction
	{

		@Override
		public void run(
				final World w,
				final BlockPos pos,
				final EnumFacing side,
				final float hitX,
				final float hitY,
				final float hitZ,
				final EntityPlayer player )
		{
			player.addChatMessage( new ChatComponentText( "canBeChiseled = " + ( api.canBeChiseled( w, pos ) ? "true" : "false" ) ) );
		}

	};

	static class isBlockChiseled extends DebugAction
	{

		@Override
		public void run(
				final World w,
				final BlockPos pos,
				final EnumFacing side,
				final float hitX,
				final float hitY,
				final float hitZ,
				final EntityPlayer player )
		{
			player.addChatMessage( new ChatComponentText( "isBlockChiseled = " + ( api.isBlockChiseled( w, pos ) ? "true" : "false" ) ) );
		}

	};

	static class getBitAccess extends DebugAction
	{

		@Override
		public void run(
				final World w,
				final BlockPos pos,
				final EnumFacing side,
				final float hitX,
				final float hitY,
				final float hitZ,
				final EntityPlayer player )
		{
			final IBitLocation loc = api.getBitPos( hitX, hitY, hitZ, side, pos, true );

			try
			{
				final IBitAccess access = api.getBitAccess( w, loc.getBlockPos() );
				final IBitBrush brush = api.createBrush( api.getBitItem( Blocks.cobblestone.getDefaultState() ) );

				access.setBitAt( loc.getBitX(), loc.getBitY(), loc.getBitZ(), brush );
				access.commitChanges( true );
			}
			catch ( final CannotBeChiseled e )
			{
				Log.logError( "FAIL", e );
			}
			catch ( final SpaceOccupied e )
			{
				Log.logError( "FAIL", e );
			}
			catch ( final InvalidBitItem e )
			{
				Log.logError( "FAIL", e );
			}
		}

	};

	static class setBitAccess extends DebugAction
	{

		@Override
		public void run(
				final World w,
				final BlockPos pos,
				final EnumFacing side,
				final float hitX,
				final float hitY,
				final float hitZ,
				final EntityPlayer player )
		{
			final IBitLocation loc = api.getBitPos( hitX, hitY, hitZ, side, pos, false );

			try
			{
				final IBitAccess access = api.getBitAccess( w, loc.getBlockPos() );
				final IBitBrush brush = api.createBrush( null );

				access.setBitAt( loc.getBitX(), loc.getBitY(), loc.getBitZ(), brush );
				access.commitChanges( true );
			}
			catch ( final CannotBeChiseled e )
			{
				Log.logError( "FAIL", e );
			}
			catch ( final SpaceOccupied e )
			{
				Log.logError( "FAIL", e );
			}
			catch ( final InvalidBitItem e )
			{
				Log.logError( "FAIL", e );
			}
		}

	};

	static class Randomize extends DebugAction
	{

		@Override
		public void run(
				final World w,
				final BlockPos pos,
				final EnumFacing side,
				final float hitX,
				final float hitY,
				final float hitZ,
				final EntityPlayer player )
		{
			final IBitLocation loc = api.getBitPos( hitX, hitY, hitZ, side, pos, false );

			try
			{
				final IBitAccess access = api.getBitAccess( w, loc.getBlockPos() );

				access.visitBits( new IBitVisitor() {

					@Override
					public IBitBrush visitBit(
							final int x,
							final int y,
							final int z,
							final IBitBrush currentValue )
					{
						IBitBrush bit = null;
						final IBlockState state = Blocks.wool.getStateFromMeta( 3 );

						try
						{
							bit = api.createBrush( api.getBitItem( state ) );
						}
						catch ( final InvalidBitItem e )
						{
						}

						return y % 2 == 0 ? currentValue : bit;
					}
				} );

				access.commitChanges( true );
			}
			catch ( final CannotBeChiseled e )
			{
				Log.logError( "FAIL", e );
			}
		}

	};

	static class getBit extends DebugAction
	{

		@Override
		public void run(
				final World w,
				final BlockPos pos,
				final EnumFacing side,
				final float hitX,
				final float hitY,
				final float hitZ,
				final EntityPlayer player )
		{
			final IBitLocation loc = api.getBitPos( hitX, hitY, hitZ, side, pos, false );

			try
			{
				final IBitAccess access = api.getBitAccess( w, loc.getBlockPos() );
				final IBitBrush brush = access.getBitAt( loc.getBitX(), loc.getBitY(), loc.getBitZ() );

				if ( brush == null )
				{
					player.addChatComponentMessage( new ChatComponentText( "AIR!" ) );
				}
				else
				{
					final IBlockState state = brush.getState();
					final Block blk = state.getBlock();

					player.inventory.addItemStackToInventory( brush.getItemStack( 1 ) );
					player.inventory.addItemStackToInventory( new ItemStack( blk, 1, blk.getMetaFromState( state ) ) );
				}
			}
			catch ( final CannotBeChiseled e )
			{
				Log.logError( "FAIL", e );
			}
		}

	};

	static class createBitItem extends DebugAction
	{

		@Override
		public void run(
				final World w,
				final BlockPos pos,
				final EnumFacing side,
				final float hitX,
				final float hitY,
				final float hitZ,
				final EntityPlayer player )
		{
			final IBitLocation loc = api.getBitPos( hitX, hitY, hitZ, side, pos, false );

			try
			{
				final IBitAccess access = api.getBitAccess( w, loc.getBlockPos() );

				player.inventory.addItemStackToInventory( access.getBitsAsItem( side, ItemType.CHISLED_BLOCK ) );
				player.inventory.addItemStackToInventory( access.getBitsAsItem( side, ItemType.MIRROR_DESIGN ) );
				player.inventory.addItemStackToInventory( access.getBitsAsItem( side, ItemType.NEGATIVE_DESIGN ) );
				player.inventory.addItemStackToInventory( access.getBitsAsItem( side, ItemType.POSITIVE_DESIGN ) );
			}
			catch ( final CannotBeChiseled e )
			{
				Log.logError( "FAIL", e );
			}
		}

	};

}