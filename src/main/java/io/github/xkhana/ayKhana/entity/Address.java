package io.github.xkhana.ayKhana.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@Entity
@Data
@Table(name = "addresses")
public class Address implements OwnableEntity {
  private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  @Column(nullable = false)
  private Type type;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @Column(nullable = false)
  private String country;

  @Column(nullable = false)
  private String governorate;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String details;

  @Column(nullable = false)
  private String zipCode;

  @Column(nullable = false)
  private String phone;

  @Column(columnDefinition = "geography(Point, 4326)", nullable = false)
  private Point location;

  @Transient
  public Double getLongitude() {
    return (location != null) ? location.getX() : 0.0;
  }

  @Transient
  public Double getLatitude() {
    return (location != null) ? location.getY() : 0.0;
  }

  @Transient
  public void setLatitude(Double latitude) {
    double currentLongitude = getLongitude();
    this.location = GEOMETRY_FACTORY.createPoint(
        new Coordinate(currentLongitude, latitude));
  }

  @Transient
  public void setLongitude(Double longitude) {
    double currentLatitude = getLatitude();
    this.location = GEOMETRY_FACTORY.createPoint(
        new Coordinate(longitude, currentLatitude));
  }

  public enum Type {
    HOME, WORK, OTHER
  }
}
