import { Component, OnInit } from '@angular/core';
import { EtudiantService } from '../../services/etudiant.service';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-etudiant-list',
  templateUrl: './etudiant-list.component.html',
  standalone: true,
  imports: [CommonModule, RouterModule, DatePipe]
})
export class EtudiantListComponent implements OnInit {
  etudiants: any[] = [];

  constructor(private etudiantService: EtudiantService) {}

  ngOnInit() {
    this.loadEtudiants();
  }

  loadEtudiants() {
    this.etudiantService.getAll().subscribe(data => {
      this.etudiants = data;
    });
  }

  deleteEtudiant(id: number) {
    if(confirm('Are you sure you want to delete this student?')) {
      this.etudiantService.delete(id).subscribe(() => {
        this.loadEtudiants();
      });
    }
  }
}