package com.testlio.lib.utility.file;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.geom.Point2D;

@Data
@AllArgsConstructor
public class FileSearchResult {
	private boolean fileFound;
	private Point2D point;
}
