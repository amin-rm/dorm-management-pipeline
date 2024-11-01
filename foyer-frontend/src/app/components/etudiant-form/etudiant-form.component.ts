import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EtudiantService } from '../../services/etudiant.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-etudiant-form',
  templateUrl: './etudiant-form.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule]
})
export class EtudiantFormComponent implements OnInit {
  etudiantForm: FormGroup;
  isEditMode = false;
  etudiantId?: number;

  constructor(
    private fb: FormBuilder,
    private etudiantService: EtudiantService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.etudiantForm = this.fb.group({
      nomEtudiant: ['', Validators.required],
      prenomEtudiant: ['', Validators.required],
      cinEtudiant: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      dateNaissance: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.etudiantId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.etudiantId) {
      this.isEditMode = true;
      this.etudiantService.getById(this.etudiantId).subscribe(etudiant => {
        this.etudiantForm.patchValue({
          ...etudiant,
          dateNaissance: new Date(etudiant.dateNaissance).toISOString().split('T')[0]
        });
      });
    }
  }

  onSubmit(): void {
    if (this.etudiantForm.valid) {
      const etudiant = this.etudiantForm.value;
      if (this.isEditMode) {
        etudiant.idEtudiant = this.etudiantId;
        this.etudiantService.update(etudiant)
          .subscribe(() => this.router.navigate(['/etudiants']));
      } else {
        this.etudiantService.create(etudiant)
          .subscribe(() => this.router.navigate(['/etudiants']));
      }
    }
  }
}