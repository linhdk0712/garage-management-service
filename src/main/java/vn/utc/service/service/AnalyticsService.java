package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.utc.service.entity.Appointment;
import vn.utc.service.entity.SparePart;
import vn.utc.service.entity.Staff;
import vn.utc.service.repo.AppointmentRepository;
import vn.utc.service.repo.CustomerRepository;
import vn.utc.service.repo.SparePartRepository;
import vn.utc.service.repo.StaffRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final SparePartRepository sparePartRepository;
    private final StaffRepository staffRepository;

    /**
     * Get dashboard statistics
     * @return Map containing dashboard statistics
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> dashboardStats = new HashMap<>();
        
        // Revenue data (simulated based on appointments and services)
        Map<String, Object> revenue = getRevenueStats();
        dashboardStats.put("revenue", revenue);

        // Customer data
        Map<String, Object> customers = getCustomerStats();
        dashboardStats.put("customers", customers);

        // Appointment data
        Map<String, Object> appointments = getAppointmentStats();
        dashboardStats.put("appointments", appointments);

        // Work order data
        Map<String, Object> workOrders = getWorkOrderStats();
        dashboardStats.put("workOrders", workOrders);

        // Inventory data
        Map<String, Object> inventory = getInventoryStats();
        dashboardStats.put("inventory", inventory);

        // Service type data
        List<Map<String, Object>> servicesByType = getServicesByType();
        dashboardStats.put("servicesByType", servicesByType);

        // Revenue by day
        List<Map<String, Object>> revenueByDay = getRevenueByDay();
        dashboardStats.put("revenueByDay", revenueByDay);

        // Staff performance
        List<Map<String, Object>> staffPerformance = getStaffPerformance();
        dashboardStats.put("staffPerformance", staffPerformance);

        return dashboardStats;
    }

    /**
     * Get performance analytics
     * @param period The time period (week, month, year)
     * @return Map containing performance analytics
     */
    public Map<String, Object> getPerformanceAnalytics(String period) {
        Map<String, Object> analyticsData = new HashMap<>();
        
        // Revenue data
        Map<String, Object> revenue = getRevenueAnalytics(period);
        analyticsData.put("revenue", revenue);

        // Customer data
        Map<String, Object> customers = getCustomerAnalytics();
        analyticsData.put("customers", customers);

        // Repairs data
        Map<String, Object> repairs = getRepairAnalytics();
        analyticsData.put("repairs", repairs);

        // Inventory data
        Map<String, Object> inventory = getInventoryAnalytics();
        analyticsData.put("inventory", inventory);

        // Staff performance data
        Map<String, Object> staffPerformance = getStaffPerformanceAnalytics();
        analyticsData.put("staffPerformance", staffPerformance);

        return analyticsData;
    }

    private Map<String, Object> getRevenueStats() {
        Map<String, Object> revenue = new HashMap<>();
        
        // Simulate revenue based on appointments and services
        long totalAppointments = appointmentRepository.count();
        BigDecimal avgServiceCost = new BigDecimal("150.00");
        BigDecimal totalRevenue = avgServiceCost.multiply(BigDecimal.valueOf(totalAppointments));
        
        revenue.put("today", totalRevenue.multiply(new BigDecimal("0.1")).doubleValue());
        revenue.put("yesterday", totalRevenue.multiply(new BigDecimal("0.09")).doubleValue());
        revenue.put("thisWeek", totalRevenue.multiply(new BigDecimal("0.6")).doubleValue());
        revenue.put("lastWeek", totalRevenue.multiply(new BigDecimal("0.55")).doubleValue());
        revenue.put("thisMonth", totalRevenue.doubleValue());
        revenue.put("lastMonth", totalRevenue.multiply(new BigDecimal("0.9")).doubleValue());
        revenue.put("changePercentage", 6.7);
        
        return revenue;
    }

    private Map<String, Object> getCustomerStats() {
        Map<String, Object> customers = new HashMap<>();
        
        long totalCustomers = customerRepository.count();
        long activeCustomers = customerRepository.count(); // Assuming all are active for now
        
        customers.put("new", (int) (totalCustomers * 0.1));
        customers.put("active", (int) activeCustomers);
        customers.put("total", (int) totalCustomers);
        customers.put("changePercentage", 8.5);
        
        return customers;
    }

    private Map<String, Object> getAppointmentStats() {
        Map<String, Object> appointments = new HashMap<>();
        
        List<Appointment> allAppointments = appointmentRepository.findAll();
        Instant now = Instant.now();
        
        long todayAppointments = allAppointments.stream()
            .filter(apt -> apt.getAppointmentDate().isAfter(now.minus(1, ChronoUnit.DAYS)))
            .count();
        
        long pendingAppointments = allAppointments.stream()
            .filter(apt -> "PENDING".equals(apt.getStatus()))
            .count();
        
        long confirmedAppointments = allAppointments.stream()
            .filter(apt -> "CONFIRMED".equals(apt.getStatus()))
            .count();
        
        long inProgressAppointments = allAppointments.stream()
            .filter(apt -> "IN_PROGRESS".equals(apt.getStatus()))
            .count();
        
        long completedAppointments = allAppointments.stream()
            .filter(apt -> "COMPLETED".equals(apt.getStatus()))
            .count();
        
        appointments.put("today", (int) todayAppointments);
        appointments.put("pending", (int) pendingAppointments);
        appointments.put("confirmed", (int) confirmedAppointments);
        appointments.put("inProgress", (int) inProgressAppointments);
        appointments.put("completed", (int) completedAppointments);
        appointments.put("cancelled", 0);
        
        return appointments;
    }

    private Map<String, Object> getWorkOrderStats() {
        Map<String, Object> workOrders = new HashMap<>();
        
        // Simulate work order stats based on appointments
        long totalAppointments = appointmentRepository.count();
        long completedAppointments = appointmentRepository.findAll().stream()
            .filter(apt -> "COMPLETED".equals(apt.getStatus()))
            .count();
        
        workOrders.put("open", (int) (totalAppointments - completedAppointments));
        workOrders.put("completed", (int) completedAppointments);
        workOrders.put("changePercentage", 12.3);
        
        return workOrders;
    }

    private Map<String, Object> getInventoryStats() {
        Map<String, Object> inventory = new HashMap<>();
        
        List<SparePart> allParts = sparePartRepository.findAll();
        long lowStockCount = allParts.stream()
            .filter(part -> part.getQuantityInStock() <= part.getMinimumStockLevel())
            .count();
        
        BigDecimal totalValue = allParts.stream()
            .map(part -> part.getPrice().multiply(BigDecimal.valueOf(part.getQuantityInStock())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<Map<String, Object>> mostUsedParts = allParts.stream()
            .limit(3)
            .map(part -> {
                Map<String, Object> map = new HashMap<>();
                map.put("name", part.getName());
                map.put("count", part.getQuantityInStock());
                return map;
            })
            .collect(Collectors.toList());
        
        inventory.put("lowStockCount", (int) lowStockCount);
        inventory.put("totalValue", totalValue.doubleValue());
        inventory.put("mostUsedParts", mostUsedParts);
        
        return inventory;
    }

    private List<Map<String, Object>> getServicesByType() {
        List<Appointment> appointments = appointmentRepository.findAll();
        
        Map<String, Long> serviceCounts = appointments.stream()
            .collect(Collectors.groupingBy(
                Appointment::getServiceType,
                Collectors.counting()
            ));
        
        return serviceCounts.entrySet().stream()
            .map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("name", entry.getKey());
                map.put("value", entry.getValue().intValue());
                return map;
            })
            .collect(Collectors.toList());
    }

    private List<Map<String, Object>> getRevenueByDay() {
        // Simulate revenue by day of week
        List<Map<String, Object>> revenueByDay = new ArrayList<>();
        
        Map<String, Object> mon = new HashMap<>();
        mon.put("name", "Mon");
        mon.put("value", 1200);
        revenueByDay.add(mon);
        
        Map<String, Object> tue = new HashMap<>();
        tue.put("name", "Tue");
        tue.put("value", 1350);
        revenueByDay.add(tue);
        
        Map<String, Object> wed = new HashMap<>();
        wed.put("name", "Wed");
        wed.put("value", 1100);
        revenueByDay.add(wed);
        
        Map<String, Object> thu = new HashMap<>();
        thu.put("name", "Thu");
        thu.put("value", 1400);
        revenueByDay.add(thu);
        
        Map<String, Object> fri = new HashMap<>();
        fri.put("name", "Fri");
        fri.put("value", 1600);
        revenueByDay.add(fri);
        
        Map<String, Object> sat = new HashMap<>();
        sat.put("name", "Sat");
        sat.put("value", 1800);
        revenueByDay.add(sat);
        
        Map<String, Object> sun = new HashMap<>();
        sun.put("name", "Sun");
        sun.put("value", 700);
        revenueByDay.add(sun);
        
        return revenueByDay;
    }

    private List<Map<String, Object>> getStaffPerformance() {
        List<Staff> allStaff = staffRepository.findAll();
        
        return allStaff.stream()
            .map(staff -> {
                Map<String, Object> map = new HashMap<>();
                map.put("name", staff.getFirstName() + " " + staff.getLastName());
                map.put("completed", (int) (Math.random() * 15) + 5);
                map.put("hours", (int) (Math.random() * 10) + 30);
                return map;
            })
            .collect(Collectors.toList());
    }

    private Map<String, Object> getRevenueAnalytics(String period) {
        Map<String, Object> revenue = new HashMap<>();
        
        // Simulate revenue data based on period
        List<Map<String, Object>> dailyRevenue = new ArrayList<>();
        
        Map<String, Object> day1 = new HashMap<>();
        day1.put("date", "2024-01-01");
        day1.put("amount", 1200);
        dailyRevenue.add(day1);
        
        Map<String, Object> day2 = new HashMap<>();
        day2.put("date", "2024-01-02");
        day2.put("amount", 1350);
        dailyRevenue.add(day2);
        
        Map<String, Object> day3 = new HashMap<>();
        day3.put("date", "2024-01-03");
        day3.put("amount", 1100);
        dailyRevenue.add(day3);
        
        Map<String, Object> day4 = new HashMap<>();
        day4.put("date", "2024-01-04");
        day4.put("amount", 1400);
        dailyRevenue.add(day4);
        
        Map<String, Object> day5 = new HashMap<>();
        day5.put("date", "2024-01-05");
        day5.put("amount", 1600);
        dailyRevenue.add(day5);
        
        Map<String, Object> day6 = new HashMap<>();
        day6.put("date", "2024-01-06");
        day6.put("amount", 1800);
        dailyRevenue.add(day6);
        
        Map<String, Object> day7 = new HashMap<>();
        day7.put("date", "2024-01-07");
        day7.put("amount", 700);
        dailyRevenue.add(day7);
        
        revenue.put("daily", dailyRevenue);
        
        List<Map<String, Object>> weeklyRevenue = new ArrayList<>();
        
        Map<String, Object> week1 = new HashMap<>();
        week1.put("week", "Week 1");
        week1.put("amount", 8750);
        weeklyRevenue.add(week1);
        
        Map<String, Object> week2 = new HashMap<>();
        week2.put("week", "Week 2");
        week2.put("amount", 9200);
        weeklyRevenue.add(week2);
        
        Map<String, Object> week3 = new HashMap<>();
        week3.put("week", "Week 3");
        week3.put("amount", 8900);
        weeklyRevenue.add(week3);
        
        Map<String, Object> week4 = new HashMap<>();
        week4.put("week", "Week 4");
        week4.put("amount", 9500);
        weeklyRevenue.add(week4);
        
        revenue.put("weekly", weeklyRevenue);
        
        List<Map<String, Object>> monthlyRevenue = new ArrayList<>();
        
        Map<String, Object> jan = new HashMap<>();
        jan.put("month", "Jan");
        jan.put("amount", 35000);
        monthlyRevenue.add(jan);
        
        Map<String, Object> feb = new HashMap<>();
        feb.put("month", "Feb");
        feb.put("amount", 38000);
        monthlyRevenue.add(feb);
        
        Map<String, Object> mar = new HashMap<>();
        mar.put("month", "Mar");
        mar.put("amount", 42000);
        monthlyRevenue.add(mar);
        
        revenue.put("monthly", monthlyRevenue);
        
        return revenue;
    }

    public Map<String, Object> getCustomerAnalytics() {
        Map<String, Object> customers = new HashMap<>();
        
        long totalCustomers = customerRepository.count();
        
        // Return data in the format expected by frontend CustomerStatistics interface
        customers.put("newCustomers", (int) (totalCustomers * 0.2));
        customers.put("activeCustomers", (int) totalCustomers);
        customers.put("averageFeedbackRating", 4.5);
        
        // Create top services data
        List<Map<String, Object>> topServices = new ArrayList<>();
        Map<String, Object> service1 = new HashMap<>();
        service1.put("serviceType", "Oil Change");
        service1.put("count", 45);
        topServices.add(service1);
        
        Map<String, Object> service2 = new HashMap<>();
        service2.put("serviceType", "Brake Service");
        service2.put("count", 32);
        topServices.add(service2);
        
        Map<String, Object> service3 = new HashMap<>();
        service3.put("serviceType", "Tire Rotation");
        service3.put("count", 28);
        topServices.add(service3);
        
        customers.put("topServices", topServices);
        
        // Create customer retention data
        Map<String, Object> customerRetention = new HashMap<>();
        customerRetention.put("singleVisit", 15);
        customerRetention.put("returning", 35);
        customerRetention.put("loyal", 50);
        customers.put("customerRetention", customerRetention);
        
        return customers;
    }

    private Map<String, Object> getRepairAnalytics() {
        Map<String, Object> repairs = new HashMap<>();
        
        List<Appointment> appointments = appointmentRepository.findAll();
        
        repairs.put("totalRepairs", appointments.size());
        repairs.put("avgCompletionTime", 2.5);
        
        Map<String, Long> repairsByType = appointments.stream()
            .collect(Collectors.groupingBy(
                apt -> apt.getServiceType().contains("Maintenance") ? "Maintenance" : 
                       apt.getServiceType().contains("Repair") ? "Repair" : "Inspection",
                Collectors.counting()
            ));
        
        List<Map<String, Object>> repairsByTypeList = repairsByType.entrySet().stream()
            .map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("type", entry.getKey());
                map.put("count", entry.getValue().intValue());
                return map;
            })
            .collect(Collectors.toList());
        repairs.put("repairsByType", repairsByTypeList);
        
        Map<String, Long> repairStatus = appointments.stream()
            .collect(Collectors.groupingBy(
                Appointment::getStatus,
                Collectors.counting()
            ));
        
        List<Map<String, Object>> repairStatusList = repairStatus.entrySet().stream()
            .map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("status", entry.getKey());
                map.put("count", entry.getValue().intValue());
                return map;
            })
            .collect(Collectors.toList());
        repairs.put("repairStatus", repairStatusList);
        
        return repairs;
    }

    private Map<String, Object> getInventoryAnalytics() {
        Map<String, Object> inventory = new HashMap<>();
        
        List<SparePart> allParts = sparePartRepository.findAll();
        
        List<Map<String, Object>> partsUsage = allParts.stream()
            .limit(4)
            .map(part -> {
                Map<String, Object> map = new HashMap<>();
                map.put("part", part.getName());
                map.put("count", part.getQuantityInStock());
                return map;
            })
            .collect(Collectors.toList());
        
        long lowStockItems = allParts.stream()
            .filter(part -> part.getQuantityInStock() <= part.getMinimumStockLevel())
            .count();
        
        BigDecimal inventoryValue = allParts.stream()
            .map(part -> part.getPrice().multiply(BigDecimal.valueOf(part.getQuantityInStock())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        inventory.put("partsUsage", partsUsage);
        inventory.put("lowStockItems", (int) lowStockItems);
        inventory.put("inventoryValue", inventoryValue.doubleValue());
        
        return inventory;
    }

    private Map<String, Object> getStaffPerformanceAnalytics() {
        Map<String, Object> staffPerformance = new HashMap<>();
        
        List<Staff> allStaff = staffRepository.findAll();
        
        List<Map<String, Object>> staffUtilization = allStaff.stream()
            .map(staff -> {
                Map<String, Object> map = new HashMap<>();
                map.put("staff", staff.getFirstName() + " " + staff.getLastName());
                map.put("utilization", (int) (Math.random() * 20) + 75);
                return map;
            })
            .collect(Collectors.toList());
        
        List<Map<String, Object>> avgTimePerRepair = allStaff.stream()
            .map(staff -> {
                Map<String, Object> map = new HashMap<>();
                map.put("staff", staff.getFirstName() + " " + staff.getLastName());
                map.put("time", Math.round((Math.random() * 1.0 + 1.5) * 10.0) / 10.0);
                return map;
            })
            .collect(Collectors.toList());
        
        staffPerformance.put("staffUtilization", staffUtilization);
        staffPerformance.put("avgTimePerRepair", avgTimePerRepair);
        
        return staffPerformance;
    }
} 