package scheduling;

import java.util.List;
import java.util.ArrayList;

public class ProcessSet 
{
	private List<AbstractProcess> fProcesses;
	
	public ProcessSet()
	{
		fProcesses = new ArrayList<AbstractProcess>();
	}
	
	public void add(AbstractProcess aProcess)
	{
		if (!fProcesses.contains(aProcess))
		{
			fProcesses.add(aProcess);
		}
	}
	
	public void remove(AbstractProcess aProcess)
	{
		if (fProcesses.contains(aProcess))
		{
			fProcesses.remove(aProcess);
		}
	}
	
	public void activate(AbstractScheduler aScheduler)
	{
		for (AbstractProcess process : fProcesses)
	    {
			process.start(aScheduler);
			process.schedule(aScheduler);
	    }
	}
	
	public boolean meetLiuAndLayland()
	{
		double lUtilisation = fProcesses.size() * (Math.pow(2,  1.0/fProcesses.size()) - 1);
		return computeUtilisation() <= lUtilisation;
	}
	
	public boolean meetCriticalInstant()
	{
		AbstractProcess lProcessM = fProcesses.get(0);
		for (AbstractProcess process : fProcesses)
	    {
			if (lProcessM.getDeadline() < process.getDeadline())
			{
				lProcessM = process;
			}
	    }
		double lUtilisation = (double)lProcessM.getComputationTime();
		for (AbstractProcess process : fProcesses)
	    {
			if (process != lProcessM)
			{
				lUtilisation += Math.ceil((double)lProcessM.getPeriod()/(double)process.getPeriod()) * (double)process.getComputationTime();
			}
		}
		return lUtilisation <= (double)lProcessM.getPeriod();
	}
	
	public double computeUtilisation()
	{
		double lUtilisation = 0;
		for (AbstractProcess process : fProcesses)
	    {
			lUtilisation += (double)process.getComputationTime() / (double)process.getPeriod();
	    }
		return lUtilisation;
	}
	
	public double computeEDFUtilization()
	{
		double lUtilisation = 0;
		for (AbstractProcess process : fProcesses)
	    {
			lUtilisation += ((double)process.getComputationTime() / Math.min((double)process.getPeriod(), (double)process.getDeadline()));
	    }
		return lUtilisation;
	}
}
