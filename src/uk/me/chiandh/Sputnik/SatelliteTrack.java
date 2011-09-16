package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;


public class SatelliteTrack {
	
	public SatellitePosition position[] = new SatellitePosition[100];
	public long startTime = 0;
	public long endTime = 0;
	private long increment = 0;
	private static double theSpherShowAllSats[] = {0.,0.,0.};
	
	
	
	public SatelliteTrack(Satellite sat, Telescope station, long startTime)
	{
		this.increment  = (long) (sat.itsSDP4.period * 60000) / 100;
		
		endTime = startTime + (long) (sat.itsSDP4.period * 60000);
		station.SetUTanyTime(startTime);
		
		
		for (int i = 0; i < 100; i++)
		{
			station.SetUTanyTime(startTime + increment * i);
    		sat.Update(station);
    		sat.GetHori(0, station, theSpherShowAllSats);
    		position[i] = new SatellitePosition("", "", 0, theSpherShowAllSats[0],
    				theSpherShowAllSats[1], theSpherShowAllSats[1] * Hmelib.DEGPERRAD, 0, sat);
    		
		}
	}



	
}
