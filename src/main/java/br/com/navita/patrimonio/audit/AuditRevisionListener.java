package br.com.navita.patrimonio.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.navita.patrimonio.model.Usuario;

public class AuditRevisionListener implements RevisionListener {

	@Override
	public void newRevision(final Object revisionEntity) {
		if (revisionEntity != null && revisionEntity instanceof AuditRevisionEntity) {
			final SecurityContext context = SecurityContextHolder.getContext();
			if (context != null) {
				final Authentication auth = context.getAuthentication();
				if (auth != null) {
					final Object principal = auth.getPrincipal();
					if (principal != null && principal instanceof Usuario) {
						final Usuario usuario = (Usuario) principal;
						final AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
						audit.setIdUsuario(usuario.getId());
						audit.setNomeUsuario(usuario.getNome());
					}
				}
			}
		}
	}

}
