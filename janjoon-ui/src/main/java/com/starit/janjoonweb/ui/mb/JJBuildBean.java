package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPhaseService;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.BuildUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.VersionDataModelUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJBuild.class, beanName = "jJBuildBean")
public class JJBuildBean {

	private int index;
	private String buildName;
	private JJBuild build;
	private List<JJBuild> builds;
	private List<JJStatus> statuts;
	private List<BuildUtil> buildUtils;
	// private List<BuildDataModel> buildDataModelList;

	@Autowired
	private JJProductService jJProductService;

	@Autowired
	private JJPhaseService jJPhaseService;

	@Autowired
	private JJStatusService jJStatusService;

	public void setjJPhaseService(JJPhaseService jJPhaseService) {
		this.jJPhaseService = jJPhaseService;
	}

	public JJProductService getjJProductService() {
		return jJProductService;
	}

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public JJBuild getBuild() {
		return build;
	}

	public void setBuild(JJBuild build) {
		this.build = build;
	}

	public List<JJBuild> getBuilds() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");

		JJVersion version = jJVersionBean.getVersion();
		builds = jJBuildService.getBuilds(((JJProductBean) session
				.getAttribute("jJProductBean")).getProduct(), version, true);

		return builds;
	}

	public void setBuilds(List<JJBuild> builds) {
		this.builds = builds;
	}

	public List<BuildUtil> getBuildUtils() {
		return buildUtils;
	}

	public void setBuildUtils(List<BuildUtil> buildUtils) {
		this.buildUtils = buildUtils;
	}

	// public List<BuildDataModel> getBuildDataModelList() {
	//
	// if (buildDataModelList == null || buildDataModelList.isEmpty())
	// initBuildDataModelList();
	// return buildDataModelList;
	// }
	//
	// public void setBuildDataModelList(List<BuildDataModel>
	// buildDataModelList) {
	// this.buildDataModelList = buildDataModelList;
	// }

	public boolean haveUrl(JJBuild build) {

		return !build.getDescription().contains("[URL]=");
	}

	public void getZipFile(JJBuild build) {

		if (build.getDescription().contains("")
				&& build.getDescription().contains(".zip")) {
			try {
				String URL = build.getDescription().substring(
						build.getDescription().indexOf("[URL]=")
								+ "[URL]=".length(),
						build.getDescription().indexOf(".zip")
								+ ".zip".length());

				ExternalContext externalContext = FacesContext
						.getCurrentInstance().getExternalContext();

				externalContext.redirect(URL);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public List<JJStatus> completeStatus(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();
		suggestions.add(null);
		if (statuts == null)
			statuts = jJStatusService.getStatus("Build", true, null, true);
		for (JJStatus jJStatus : statuts) {
			String jJStatusStr = String.valueOf(jJStatus.getName() + " "
					+ jJStatus.getDescription() + " "
					+ jJStatus.getCreationDate() + " "
					+ jJStatus.getUpdatedDate());
			if (jJStatusStr.toLowerCase().contains(query.toLowerCase())) {
				suggestions.add(jJStatus);
			}
		}
		return suggestions;
	}

	public void changeEvent(JJBuild b, VersionDataModelUtil buildDataModel) {
		updateJJBuild(b);
		buildDataModel.getBuilds().set(buildDataModel.getBuilds().indexOf(b),
				jJBuildService.findJJBuild(b.getId()));
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Build","");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void loadingRowExpansion(JJVersion v) {

		if (buildUtils == null)
			buildUtils = new ArrayList<BuildUtil>();

		JJVersion version = null;

		if (v.getId() != null)
			version = jJVersionService.findJJVersion(v.getId());

		if (version == null) {
			v.setProduct(((JJProductBean) LoginBean.findBean("jJProductBean"))
					.getProductAdmin());
			((JJVersionBean) LoginBean.findBean("jJVersionBean"))
					.saveJJVersion(v);
			version = jJVersionService.findJJVersion(v.getId());
		}
		long l = version.getId();

		int i = BuildUtil.BuildUtil(version, buildUtils);
		if (i == -1)
			buildUtils.add(new BuildUtil(version, jJBuildService.getBuilds(
					version, true, true)));

		index = BuildUtil.BuildUtil(jJVersionService.findJJVersion(l),
				buildUtils);
		System.out.println(index + "/" + l);
		System.out.println("size :"
				+ buildUtils.get(index).getVersionBuilds().size());

	}

	public void rowToggle() {

		System.err.println("rowToggle");
	}

	public void updateVersionBuilds(long id) {

		int i = BuildUtil.BuildUtil(jJVersionService.findJJVersion(id),
				buildUtils);
		BuildUtil buildutil = buildUtils.get(i);
		buildutil.updateVersionBuilds(this);
		buildUtils.set(
				i,
				new BuildUtil(buildutil.getVersion(), jJBuildService.getBuilds(
						buildutil.getVersion(), true, true)));
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Version","e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void addBuild(Long l) {

		int i = BuildUtil.BuildUtil(jJVersionService.findJJVersion(l),
				buildUtils);
		BuildUtil buildUtil = buildUtils.get(i);
		JJVersion version = buildUtil.getVersion();
		JJBuild b = new JJBuild();
		b.setName(buildName);
		b.setEnabled(true);
		b.setVersion(buildUtil.getVersion());
		b.setDescription("Build for Version " + version.getName());

		if (saveJJBuild(b)) {
			buildName = null;
			buildUtils.set(
					i,
					new BuildUtil(version, jJBuildService.getBuilds(version,
							true, true)));

			index = i;
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_created", "Bug","");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nom Exist",
							"Build"));
		}

	}

	// public String getHeight() {
	// if (buildDataModelList != null) {
	// int i = buildDataModelList.size() / 3;
	// int j = buildDataModelList.size() % 3;
	//
	// if (j != 0)
	// i = i + 1;
	// return 300 * i + 100 + "px";
	// } else
	// return "100px";
	//
	// }

	// public void initBuildDataModelList() {
	// LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
	// buildDataModelList = new ArrayList<BuildDataModel>();
	// statuts = jJStatusService.getStatus("Build", true, null, true);
	// if (LoginBean.getProduct() == null) {
	//
	// for (JJProduct prod : jJProductService.getProducts(loginBean
	// .getContact().getCompany(), LoginBean.getProject())) {
	// List<JJVersion> versions = jJVersionService.getVersions(true,
	// true, prod, loginBean.getContact().getCompany(), true);
	// for (JJVersion version : versions) {
	// buildDataModelList.add(new BuildDataModel(version,jJBuildService));
	// }
	// }
	//
	// } else if (LoginBean.getVersion() == null) {
	// List<JJVersion> versions = jJVersionService.getVersions(true, true,
	// LoginBean.getProduct(),
	// loginBean.getContact().getCompany(), true);
	// for (JJVersion version : versions) {
	// buildDataModelList.add(new BuildDataModel(version,jJBuildService));
	// }
	//
	// } else {
	// buildDataModelList.add(new
	// BuildDataModel(LoginBean.getVersion(),jJBuildService));
	// }
	// }

	public boolean buildNameExist(String name, JJVersion version) {
		if (version != null)
			return jJBuildService.getBuildByName(version, name) != null;
		else
			return true;
	}

	public boolean updateJJBuild(JJBuild b) {

		if (b.getVersion() == null) {
			JJContact contact = ((LoginBean) ((HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false)).getAttribute("loginBean")).getContact();
			b.setUpdatedBy(contact);
			b.setUpdatedDate(new Date());
			jJBuildService.updateJJBuild(b);
			return true;

		} else {
			JJBuild buil = jJBuildService.getBuildByName(b.getVersion(),
					b.getName());

			if (buil != null) {
				if (buil.getId().equals(b.getId())) {

					JJContact contact = ((LoginBean) ((HttpSession) FacesContext
							.getCurrentInstance().getExternalContext()
							.getSession(false)).getAttribute("loginBean"))
							.getContact();
					b.setUpdatedBy(contact);
					b.setUpdatedDate(new Date());
					jJBuildService.updateJJBuild(b);
					return true;
				} else
					return false;

			} else {
				JJContact contact = ((LoginBean) ((HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false)).getAttribute("loginBean"))
						.getContact();
				b.setUpdatedBy(contact);
				b.setUpdatedDate(new Date());
				jJBuildService.updateJJBuild(b);
				return true;
			}
		}

	}

	public boolean saveJJBuild(JJBuild b) {

		if (!buildNameExist(b.getName(), b.getVersion())) {
			b.setCreationDate(new Date());
			JJContact contact = ((LoginBean) ((HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false)).getAttribute("loginBean")).getContact();
			b.setCreatedBy(contact);
			jJBuildService.saveJJBuild(b);
			return true;
		} else
			return false;

	}

}
