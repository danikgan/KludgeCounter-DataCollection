package second.process.data;

public class BugzillaOverview {
	private String project;
	private Integer id;
	private String product;
	private String assigned_to;
	private String[] cc;
	private String classification;
	private String component;
	private String creator;
	private String creation_time;
	private String is_open;
	private String last_change_time;
	private String priority;
	private String resolution;
	private String severity;
	private String status;
	private String summary;
	private String version;

	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getAssigned_to() {
		return assigned_to;
	}

	public void setAssigned_to(String assigned_to) {
		this.assigned_to = assigned_to;
	}

	public String getCc() {
		StringBuilder newCC = new StringBuilder("");
		for (String cc_single:cc) {
			newCC.append(cc_single);
			if (cc_single != cc[cc.length-1]) { // supposed to be "!=" and not "equals"
				newCC.append("\n"); // doesn't add "\n" only to the last value
			}
		}
//		if (!newCC.toString().equals("")) {
//			newCC.delete(newCC.toString().length()-1,newCC.toString().length()); // removing extra "\n" at the end
//		}
		return newCC.toString();
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(String creation_time) {
		this.creation_time = creation_time;
	}

	public String getIs_open() {
		return is_open;
	}

	public void setIs_open(String is_open) {
		this.is_open = is_open;
	}

	public String getLast_change_time() {
		return last_change_time;
	}

	public void setLast_change_time(String last_change_time) {
		this.last_change_time = last_change_time;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
