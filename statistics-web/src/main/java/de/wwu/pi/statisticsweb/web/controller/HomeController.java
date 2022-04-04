package de.wwu.pi.statisticsweb.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.wwu.pi.statisticsweb.service.StatisticsService;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private StatisticsService statisticsService;
	
	@GetMapping
	/**
	 * Display the statistics of so far stored DataPoints.
	 * @param model
	 * @return
	 */
	public String index(Model model) {
		model.addAttribute("average", statisticsService.getAverage());
		model.addAttribute("minimum", statisticsService.getMinimum());
		model.addAttribute("maximum", statisticsService.getMaximum());
		return "home/index";
	}

	@PostMapping
	/**
	 * Allow to push new DataPoints to the statistics.
	 * @param Double x
	 * @return Redirects to home
	 */
	public String index(Double x) {
		if (x == null) {
			x = 0.0;
		}
		statisticsService.addStatistics(x);
		return "redirect:/";
	}
}
