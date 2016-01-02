package mod.chiselsandbits.helpers;

import net.minecraft.util.StatCollector;

public enum LocalStrings
{

	ChiselModeSingle( "chiselmode.single" ),
	ChiselModeSnap2( "chiselmode.snap2" ),
	ChiselModeSnap4( "chiselmode.snap4" ),
	ChiselModeSnap8( "chiselmode.snap8" ),
	ChiselModeLine( "chiselmode.line" ),
	ChiselModePlane( "chiselmode.plane" ),
	ChiselModeConnectedPlane( "chiselmode.connected_plane" ),
	ChiselModeCubeSmall( "chiselmode.cube_small" ),
	ChiselModeCubeMedium( "chiselmode.cube_medium" ),
	ChiselModeCubeLarge( "chiselmode.cube_large" ),
	ChiselModeDrawnRegion( "chiselmode.drawn_region" ),

	ShiftDetails( "help.shiftdetails" ),
	Empty( "help.empty" ),
	Solid( "help.solid" ),

	HelpChiseledBlock( "help.chiseled_block" ),
	LongHelpChiseledBlock( "help.chiseled_block.long" ),

	HelpBitBag( "help.bit_bag" ),
	LongHelpBitBag( "help.bit_bag.long" ),

	HelpWrench( "help.wrench" ),
	LongHelpWrench( "help.wrench.long" ),

	HelpBit( "help.bit" ),
	LongHelpBit( "help.bit.long" ),

	HelpPositivePrint( "help.positiveprint" ),
	LongHelpPositivePrint( "help.positiveprint.long" ),

	HelpNegativePrint( "help.negativeprint" ),
	LongHelpNegativePrint( "help.negativeprint.long" ),

	HelpMirrorPrint( "help.mirrorprint" ),
	LongHelpMirrorPrint( "help.mirrorprint.long" ),

	HelpChisel( "help.chisel" ),
	LongHelpChisel( "help.chisel.long" );

	private final String string;

	private LocalStrings(
			final String postFix )
	{
		string = "mod.chiselsandbits." + postFix;
	}

	@Override
	public String toString()
	{
		return string;
	}

	public String getLocal()
	{
		return StatCollector.translateToLocal( string );
	}

}
