import java.util.*;

class Event {

	private Integer id;
	private ArrayList<Ticket> tickets;

	public Event(Integer id) {
		this.setId(id);
		this.tickets = new ArrayList<Ticket>();
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ArrayList<Ticket> getTickets() {
		return tickets;
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	public String toString() {
		return Integer.toString(id);
	}

	public void generateRandomTickets() {
		
		Random rand = new Random();
		// Generates random number of tickets between 0 and 20.
		int nrTickets = rand.nextInt(20);

		for(int i = 0; i < 20; ++i) {
			// Generates random price between 1 and 250.
			Double randomPrice = 1 + (250 - 1) * rand.nextDouble();
			tickets.add(new Ticket(randomPrice));
		}

	}

	public Ticket getCheapestTicket() {
		
		// If there are no tickets, return null.
		if(tickets.size() == 0) return null;

		Ticket minPriceTicket = tickets.get(0);

		for(int i = 0; i < tickets.size(); ++i) {
		
			Ticket currentTicket = tickets.get(i);

			if(currentTicket.getPrice() < minPriceTicket.getPrice())
				minPriceTicket = currentTicket;
		}

		return minPriceTicket;
	}

}