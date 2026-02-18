package org.univ_paris8.iut.montreuil.dev_avance.api;

import org.univ_paris8.iut.montreuil.dev_avance.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.mapper.AnnonceMapper;
import org.univ_paris8.iut.montreuil.dev_avance.service.AnnoncesService;
import org.univ_paris8.iut.montreuil.dev_avance.security.Secured;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/annonces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnnonceResource {

    private AnnoncesService service = new AnnoncesService();

    @GET
    public Response getAll(@QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        List<Annonce> entities = service.getAnnonces(page, size);
        List<AnnonceDTO> dtos = entities.stream()
                .map(AnnonceMapper::toDTO)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getOne(@PathParam("id") Long id) {
        Annonce entity = service.getAnnonce(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(AnnonceMapper.toDTO(entity)).build();
    }

    @POST
    @Secured
    public Response create(@Valid AnnonceDTO dto, @Context UriInfo uriInfo, @Context SecurityContext securityContext) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Annonce entity = AnnonceMapper.toEntity(dto);

        String userIdStr = securityContext.getUserPrincipal().getName();
        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        service.createAnnonce(entity, dto.getCategoryId(), userId);

        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(entity.getId())).build();
        return Response.created(location).entity(AnnonceMapper.toDTO(entity)).build();
    }

    @PUT
    @Path("/{id}")
    @Secured
    public Response update(@PathParam("id") Long id, @Valid AnnonceDTO dto, @Context SecurityContext securityContext) {
        Annonce entity = service.getAnnonce(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String userIdStr = securityContext.getUserPrincipal().getName();
        Long currentUserId = Long.parseLong(userIdStr);
        if (entity.getAuthor() != null && !entity.getAuthor().getId().equals(currentUserId)) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"Only author can modify\"}").build();
        }

        if (entity.getStatus() == Annonce.Status.PUBLISHED) {
            return Response.status(Response.Status.CONFLICT).entity("{\"error\": \"Cannot modify PUBLISHED annonce\"}")
                    .build();
        }

        AnnonceMapper.updateEntity(entity, dto);
        service.updateAnnonce(entity);

        return Response.ok(AnnonceMapper.toDTO(entity)).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    public Response delete(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        Annonce entity = service.getAnnonce(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String userIdStr = securityContext.getUserPrincipal().getName();
        Long currentUserId = Long.parseLong(userIdStr);
        if (entity.getAuthor() != null && !entity.getAuthor().getId().equals(currentUserId)) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"Only author can delete\"}").build();
        }

        if (entity.getStatus() != Annonce.Status.ARCHIVED) {
            return Response.status(Response.Status.CONFLICT).entity("{\"error\": \"Must be ARCHIVED before deletion\"}")
                    .build();
        }

        service.deleteAnnonce(id);
        return Response.noContent().build();
    }
}
