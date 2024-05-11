package scheduling;

public class EDFScheduler extends AbstractScheduler 
{

	public EDFScheduler() 
	{
		super("EDFScheduler");
	}

	@Override
	protected void add(AbstractProcess aProcess) 
	{
		fInactiveQueue.remove(aProcess);
		aProcess.setSuspended();
		
		int i = 0;
		for (i = 0; i < fActiveQueue.size(); i++) 
		{
			if (aProcess.getNextDeadline() < fActiveQueue.get(i).getNextDeadline()) 
			{
				break;
            }
        }
		
		if (!fActiveQueue.isEmpty() && i == 0)
		{
			fActiveQueue.get(0).setSuspended();
		}
        fActiveQueue.add(i, aProcess);
		fActiveQueue.get(0).setRunning();
	}

	@Override
	protected void remove(AbstractProcess aProcess) 
	{
		aProcess.setInactive();
		fActiveQueue.remove(aProcess);
		fInactiveQueue.add(aProcess);
		if (!fActiveQueue.isEmpty())
		{
			fActiveQueue.get(0).setRunning();
		}
	}
}
