package Otopark;

import java.util.ArrayList;

public class AutoPark {
	private ArrayList<SubscribedVehicle> subscribedVehicles;
	private ArrayList<ParkRecord> parkRecords;
	private double hourlyFee, incomeDaily;
	private int capacity;
	private int currentCapacity;


	private static AutoPark single_instance = null;


	public static void setSingle_instance(AutoPark single_instance) {
		AutoPark.single_instance = single_instance;
	}

	public static AutoPark getInstance(){
		if (single_instance == null)
			single_instance = new AutoPark(0,0);

		return single_instance;
	}

	public AutoPark( double hourlyFee,int capacity) {
		this.subscribedVehicles = new ArrayList <SubscribedVehicle> ();
		this.parkRecords = new ArrayList<ParkRecord>();
		this.hourlyFee = hourlyFee;
		this.incomeDaily = 0;
		this.capacity = capacity;
		this.currentCapacity = capacity;
	}

	public SubscribedVehicle searchVehicle(String plate) {
		for (SubscribedVehicle vehicle: subscribedVehicles) {
			if (vehicle.getPlate().equals(plate))
				return vehicle;
		}
		return null;
	}

	public boolean isParked (String plate) {
		for (ParkRecord record : parkRecords) {
			if (record.getVehicle().getPlate().equals(plate)) {
				if (record.getEnterDate().isBeforeThan(Date.getToday()) && record.getExitDate() == null)
					return true;
			}
		}
		return false;
	}

	public ParkRecord searchParkRecords (String plate) { //ihtiyac oldugundan biz ekledik
		for (ParkRecord record : parkRecords) {
			if (record.getVehicle().getPlate().equals(plate)) {
				if (record.getEnterDate().isBeforeThan(Date.getToday()) && record.getExitDate() == null)
					return record;
			}
		}
		return null;
	}


	public boolean addVehicle (SubscribedVehicle vehicle) {
		for(SubscribedVehicle aVehicle: subscribedVehicles) {
			if ( aVehicle.getPlate().equals( vehicle.getPlate() ))
				return false;
		}
		subscribedVehicles.add(vehicle);
		return true;
	}

	public boolean vehicleEnters(String plate, Time enter, boolean isOfficial,  Date enterDate) throws FullCapacityException, AlreadyParkedException{
		if (!isParked(plate)) {
			if (currentCapacity == 0) {
				throw new FullCapacityException("The capacity is full!");
			}
			if (searchVehicle(plate) != null) {
				SubscribedVehicle aVehicle = searchVehicle(plate);
				ParkRecord record = new ParkRecord(enter, aVehicle, enterDate);
				parkRecords.add(record);
				currentCapacity--;
				return true;
			}
			else if (isOfficial) {
				OfficialVehicle aVehicle = new OfficialVehicle(plate);
				ParkRecord record = new ParkRecord(enter, aVehicle, enterDate);
				parkRecords.add(record);
				currentCapacity--;
				return true;
			}
			else {
				RegularVehicle aVehicle = new RegularVehicle(plate);
				ParkRecord record = new ParkRecord(enter, aVehicle, enterDate);
				parkRecords.add(record);
				currentCapacity--;
				return true;
			}

		}
		throw new AlreadyParkedException("This vehicle is already parked");
	}

	public boolean vehicleExits (String plate, Date exitDate, Time exitTime) throws VehicleIsNotParkedException {
		ParkRecord record = searchParkRecords(plate);

		if(record!=null){
			record.setExitDate(exitDate);
			record.setExitTime(exitTime);
			currentCapacity++;
			if (!record.getVehicle().isOfficial() && record.getVehicle().getSubscription() == null || !record.getVehicle().getSubscription().isValid()) {
				System.out.println("Duration " + record.getParkingDuration());
				if (record.getParkingDuration() > 55) {
					System.out.println("Duration " + record.getParkingDuration());
					this.incomeDaily += record.getParkingDuration()/60 * hourlyFee;
					System.out.println(record.getParkingDuration());
					return true;
				}
			}
		}
		throw new VehicleIsNotParkedException("Vehicle is not parked in auto park!");

	}


	public void setHourlyFee(double hourlyFee) {
		this.hourlyFee = hourlyFee;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public double getIncomeDaily() {
		return incomeDaily;
	}

	public double getHourlyFee() {
		return hourlyFee;
	}

	public int getCurrentCapacity() {
		return currentCapacity;
	}


	@Override
	public String toString() {
		return "AutoPark [ hourlyFee="+ hourlyFee + ", incomeDaily=" + incomeDaily + "]" + ", capacity=" + capacity;
	}

	public ArrayList<SubscribedVehicle> getSubscribedVehicles() {
		return subscribedVehicles;
	}

	public ArrayList<ParkRecord> getParkRecords() {
		return parkRecords;
	}



	public void introduceSelf() {
		System.out.println( toString() );
		System.out.println("Subscribed vehicles : ");
		for (SubscribedVehicle aVehicle: subscribedVehicles)
			System.out.println(aVehicle.toString());
		System.out.println("Park records : ");
		for(ParkRecord records : parkRecords)
			System.out.println(records.toString());
	}


}
