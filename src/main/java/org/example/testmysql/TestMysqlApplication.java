package org.example.testmysql;

import org.example.testmysql.pojo.Area;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.example.testmysql.service.UserService;
import org.example.testmysql.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

@SpringBootApplication
public class TestMysqlApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(TestMysqlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		while (true) {
			System.out.println("请选择操作：");
			System.out.println("1. 获取所有用户数据");
			System.out.println("2. 获取时间段内的活跃用户列表");
			System.out.println("3. 获取指定区域内的用户列表");
			System.out.println("4. 分析高峰时间段");
			System.out.println("5. 获取上海理工大学各区域用户");
			System.out.println("6. 退出");
			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
				case 1:
					List<User> users = userService.getAllUsers();
					System.out.println("用户总数: " + users.size());
					users.forEach(System.out::println);
					break;
				case 2:
					System.out.println("请输入开始时间 (格式: yyyy-MM-dd HH:mm:ss): ");
					String startStr = scanner.nextLine();
					if (!isValidDateTime(startStr, formatter)) {
						System.out.println("输入的开始时间格式不正确，请重新输入。");
						continue;
					}
					LocalDateTime start = LocalDateTime.parse(startStr, formatter);

					System.out.println("请输入结束时间 (格式: yyyy-MM-dd HH:mm:ss): ");
					String endStr = scanner.nextLine();
					if (!isValidDateTime(endStr, formatter)) {
						System.out.println("输入的结束时间格式不正确，请重新输入。");
						continue;
					}
					LocalDateTime end = LocalDateTime.parse(endStr, formatter);

					List<User> activeUsers = userService.getUsersInTimeRange(Timestamp.valueOf(start), Timestamp.valueOf(end));
					System.out.println("活跃用户总数: " + activeUsers.size());
					System.out.println("活跃用户列表: ");
					activeUsers.forEach(System.out::println);
					break;
				case 3:
					System.out.println("请输入最小经度: ");
					double minLongitude = scanner.nextDouble();
					System.out.println("请输入最大经度: ");
					double maxLongitude = scanner.nextDouble();
					System.out.println("请输入最小纬度: ");
					double minLatitude = scanner.nextDouble();
					System.out.println("请输入最大纬度: ");
					double maxLatitude = scanner.nextDouble();
					List<User> usersInRegion = userService.getUsersInRegion(minLongitude, maxLongitude, minLatitude, maxLatitude);
					System.out.println("指定区域内的用户总数: " + usersInRegion.size());
					System.out.println("指定区域内的用户列表: ");
					usersInRegion.forEach(System.out::println);
					break;
				case 4:
					System.out.println("请输入开始时间 (格式: yyyy-MM-dd HH:mm:ss): ");
					String analysisStartStr = scanner.nextLine().trim();
					if (!isValidDateTime(analysisStartStr, formatter)) {
						System.out.println("输入的开始时间格式不正确，请重新输入。");
						continue;
					}
					LocalDateTime analysisStart = LocalDateTime.parse(analysisStartStr, formatter);

					System.out.println("请输入结束时间 (格式: yyyy-MM-dd HH:mm:ss): ");
					String analysisEndStr = scanner.nextLine().trim();
					if (!isValidDateTime(analysisEndStr, formatter)) {
						System.out.println("输入的结束时间格式不正确，请重新输入。");
						continue;
					}
					LocalDateTime analysisEnd = LocalDateTime.parse(analysisEndStr, formatter);

					analyzePeakHours(analysisStart, analysisEnd);
					break;
				case 5:
				displayUsersInShanghaitechAreas();
				break;
				case 6:
					System.out.println("退出程序");
					scanner.close();
					return;
				default:
					System.out.println("无效的选择，请重新输入");
			}
		}
	}
	private boolean isValidDateTime(String dateTimeStr, DateTimeFormatter formatter) {
		try {
			LocalDateTime.parse(dateTimeStr, formatter);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
	private void analyzePeakHours(LocalDateTime start, LocalDateTime end) {
		Map<LocalDateTime, Integer> hourlyUserCounts = new HashMap<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// 获取指定时间段内的所有用户
		List<User> users = userService.getUsersInTimeRange(Timestamp.valueOf(start), Timestamp.valueOf(end));

		for (User user : users) {
			LocalDateTime userTime = user.getUploadTime().toLocalDateTime();
			LocalDateTime hourStart = userTime.truncatedTo(ChronoUnit.HOURS);
			hourlyUserCounts.put(hourStart, hourlyUserCounts.getOrDefault(hourStart, 0) + 1);
		}

		// 找出高峰时间段
		List<LocalDateTime> peakHours = findPeakHours(hourlyUserCounts);

		System.out.println("高峰时间段:");
		for (LocalDateTime peakHour : peakHours) {
			System.out.println(peakHour.format(formatter) + " - " + peakHour.plusHours(1).format(formatter));
		}
	}

	private List<LocalDateTime> findPeakHours(Map<LocalDateTime, Integer> hourlyUserCounts) {
		List<LocalDateTime> peakHours = new ArrayList<>();
		int threshold = 3; // 假设高峰时间段的用户数阈值为3，可以根据实际情况调整

		LocalDateTime currentPeakStart = null;
		LocalDateTime currentPeakEnd = null;

		for (Map.Entry<LocalDateTime, Integer> entry : hourlyUserCounts.entrySet()) {
			LocalDateTime hour = entry.getKey();
			int count = entry.getValue();

			if (count >= threshold) {
				if (currentPeakStart == null) {
					currentPeakStart = hour;
				}
				currentPeakEnd = hour;
			} else {
				if (currentPeakStart != null) {
					peakHours.add(currentPeakStart);
					peakHours.add(currentPeakEnd);
					currentPeakStart = null;
					currentPeakEnd = null;
				}
			}
		}

		if (currentPeakStart != null && currentPeakEnd != null) {
			peakHours.add(currentPeakStart);
			peakHours.add(currentPeakEnd);
		}

		return peakHours;
	}
	private void displayUsersInShanghaitechAreas() {
		List<User> allUsers = userService.getAllUsers();

		// 定义上海理工大学的五个区域
		Area area1 = new Area(121.55703, 31.29612); // 五食堂
		Area area2 = new Area(121.556382, 31.29247); // 毛主席像
		Area area3 = new Area(121.554396, 31.295199); // 湛恩图书馆
		Area area4 = new Area(121.557254, 31.289346); // 思餐厅
		Area area5 = new Area(121.554831, 31.288673); // 卓越楼

		List<Area> areas = List.of(area1, area2, area3, area4, area5);

		for (Area area : areas) {
			List<User> usersInArea = new ArrayList<>();
			for (User user : allUsers) {
				if (area.contains(user)) {
					usersInArea.add(user);
				}
			}
			System.out.println("区域: " + area);
			System.out.println("用户总数: " + usersInArea.size());
			System.out.println("用户列表: ");
			usersInArea.forEach(System.out::println);
			System.out.println();
		}
	}


}


