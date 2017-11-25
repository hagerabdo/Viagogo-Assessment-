import java.util.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

class Main {

	// Stores the events per location.
	public static HashMap<Location,Event> world;
	// Max heap tree that will help compute the closest 5 events.
	public static PriorityQueue<AbstractMap.SimpleEntry<Event,Integer>> closestEvents;
	// Stores existing locations, so we can prevent adding more than one event to one of them.
	public static ArrayList<AbstractMap.SimpleEntry<Integer,Integer>> locations;

	public static void main(String[] args) {

		// Generates seed data.
		generateSeedData();

		// Input location coordinates.
		Scanner scan = new Scanner(System.in);

		System.out.print("Please Input x Coordinate: ");
		int x =  scan.nextInt();

		System.out.print("Please Input y Coordinate: ");
		int y = scan.nextInt();

		Location myLocation = new Location(x,y);

		// Initalises comparator.
		EventsPriority eventsPriority = new EventsPriority();
		// Creates a max-heap tree of max-size 5, using the comparator above.
		closestEvents = new PriorityQueue<AbstractMap.SimpleEntry<Event,Integer>>(5,eventsPriority);

		// Iterates through all the events.
		for (Map.Entry<Location, Event> entry : world.entrySet()) {
			
			// Retrieves the location and event from hashmap.
			Location location = entry.getKey();
			Event event = entry.getValue();

			// Computes the distance between my location and the event location.
			Integer distance = manhattanDistance(myLocation,location);

			// If there are less than 5 events in the heap, just adds the current event without any verification.
			if(closestEvents.size() < 5)
				closestEvents.add(new AbstractMap.SimpleEntry<Event,Integer>(event,distance));
			// Else, if the distance to the current event is less than the distance to the event that is the head of the heap tree,
			// then we want to eliminate that event from the top of the heap and insert the current one.
			else if(distance < closestEvents.peek().getValue()) { 

				// Removes the top of the heap tree.
				if(closestEvents.size() == 5) closestEvents.poll();

				// Inserts the current (event,distance) pair in the heap.
				closestEvents.add(new AbstractMap.SimpleEntry<Event,Integer>(event,distance));
			}
		}

		// Shows results.
		for (AbstractMap.SimpleEntry<Event,Integer> entry : closestEvents) {
			Event event = entry.getKey();
			Double cheapestPrice = event.getCheapestTicket().getPrice();
			String priceText;

			if(cheapestPrice == null) priceText = "No tickets available";
			else {
				NumberFormat formatter = new DecimalFormat("#0.00");     
				priceText = "$"+formatter.format(cheapestPrice);
			}

			System.out.println("Event #"+event + " - "+priceText+", Distance: "+entry.getValue());
		}

	}

	// Calculates the manhattan distance between 2 locations.
	public static Integer manhattanDistance(Location l1, Location l2) {
		return Math.abs(l1.getX() - l2.getX()) + Math.abs(l1.getY() - l2.getY());
	}

	public static void generateSeedData() {

		// Init world.
		world = new HashMap<Location,Event>();
		locations = new ArrayList<AbstractMap.SimpleEntry<Integer,Integer>>();

		Random rand = new Random();

		// Generates a random number of events between 1 and 70.
		int nrEvents = rand.nextInt(50) + 1;

		for (int i = 1; i <= nrEvents; ++i)
		{
			int eventX = rand.nextInt(10) + (-10);
			int eventY = rand.nextInt(10) + (-10);

			Event event = new Event(i);
			// Generates random tickets for this event.
			event.generateRandomTickets();

			Location location = new Location(eventX,eventY);

			// Checks that the location has not been added already.
			AbstractMap.SimpleEntry<Integer,Integer> entry = new AbstractMap.SimpleEntry<Integer,Integer>(eventX,eventY);
			if(!locations.contains(entry))
			{
				locations.add(entry);
				world.put(location,event);
			}
		}
	}

}

// Used as a comparator for the Heap tree that will compute the closest 5 events.
class EventsPriority implements Comparator<AbstractMap.SimpleEntry<Event,Integer>>
{
    public int compare(AbstractMap.SimpleEntry<Event,Integer> entry1, AbstractMap.SimpleEntry<Event,Integer> entry2) {
        if (entry1.getValue() < entry2.getValue()) return +1;
        if (entry1.getValue().equals(entry2.getValue())) return 0;
        return -1;
    }
}
