import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EtudiantListComponent } from './components/etudiant-list/etudiant-list.component';
import { EtudiantFormComponent } from './components/etudiant-form/etudiant-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'etudiants', pathMatch: 'full' },
  { path: 'etudiants', component: EtudiantListComponent },
  { path: 'etudiant/new', component: EtudiantFormComponent },
  { path: 'etudiant/edit/:id', component: EtudiantFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }