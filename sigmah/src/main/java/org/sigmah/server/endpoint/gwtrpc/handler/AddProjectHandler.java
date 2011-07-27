package org.sigmah.server.endpoint.gwtrpc.handler;

//
///*
// * Adds given Project to the database
// */
//public class AddProjectHandler implements CommandHandler<AddProject> {
//
//    private final EntityManager em;
//
//    @Inject
//	public AddProjectHandler(EntityManager em) {
//		this.em = em;
//	}
//
//	@Override
//	public CommandResult execute(AddProject cmd, User user)
//			throws CommandException {
//
//        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());
//
//        Project2DTO from = cmd.getProject2DTO();
//        Project2 project = new Project2();
//        project.setName(from.getName());
//        project.setDescription(from.getDescription());
//
//        em.persist(project);
//        db.getProjects().add(project);
//
//        return new CreateResult(project.getId());
//	}
//}