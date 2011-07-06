package models;

import javax.persistence.*;
import play.db.jpa.*;
import java.io.File;

@Entity
public class Picture extends Model {
	public String fileName;
	public DateBlob image;
}