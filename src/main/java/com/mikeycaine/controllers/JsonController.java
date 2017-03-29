package com.mikeycaine.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


@RestController
public class JsonController {
	
	@RequestMapping("/jsondata")
	public String jsondata() throws SchemaException, IOException, NoSuchAuthorityCodeException, FactoryException {
		Map<String, Point> pointMap = new HashMap<String, Point>();
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Point admiralTowerPoint = geometryFactory.createPoint(new Coordinate(537749.85, 177861.19));
		Point barrowGatePoint = geometryFactory.createPoint(new Coordinate(326865, 520251));
		
		pointMap.put("Admiral's Tower", admiralTowerPoint);
		pointMap.put("Barrow Gate", barrowGatePoint);
		
		return jsonFeatureCollectionFromPointMap(pointMap);
	}
	
	private String jsonFeatureCollectionFromPointMap(Map<String, Point> pointMap) throws NoSuchAuthorityCodeException, FactoryException, IOException {
		
	    CoordinateReferenceSystem osgbCrs = CRS.decode("EPSG:27700");
		
		SimpleFeatureTypeBuilder pointTypeBuilder = new SimpleFeatureTypeBuilder();
		pointTypeBuilder.setName("Location");
		pointTypeBuilder.setCRS(osgbCrs);
		pointTypeBuilder.add("location", Point.class);
		pointTypeBuilder.add("name", String.class);
		
		final SimpleFeatureType pointType = pointTypeBuilder.buildFeatureType();
		
		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(null,null);
		
		for (Map.Entry<String, Point> entry : pointMap.entrySet()) {
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(pointType);
			featureBuilder.add(entry.getValue());
			featureBuilder.add(entry.getKey());
			SimpleFeature feature = featureBuilder.buildFeature(null);
			featureCollection.add(feature);
		}
		
	    FeatureJSON fj = new FeatureJSON();
	    fj.setEncodeFeatureCRS(true);
	    
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    fj.writeFeatureCollection(featureCollection, os);
		
		return os.toString();
		
	}
}
