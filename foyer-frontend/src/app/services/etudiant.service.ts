import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Etudiant } from '../models/etudiant';

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {
  //private baseUrl = 'http://localhost:8089/tpfoyer/etudiant';
  private baseUrl = 'http://192.168.119.128:8089/tpfoyer/etudiant';
  constructor(private http: HttpClient) { }

  getAll(): Observable<Etudiant[]> {
    return this.http.get<Etudiant[]>(`${this.baseUrl}/retrieve-all-etudiants`);
  }

  getById(id: number): Observable<Etudiant> {
    return this.http.get<Etudiant>(`${this.baseUrl}/retrieve-etudiant/${id}`);
  }

  getByCin(cin: number): Observable<Etudiant> {
    return this.http.get<Etudiant>(`${this.baseUrl}/retrieve-etudiant-cin/${cin}`);
  }

  create(etudiant: Etudiant): Observable<Etudiant> {
    return this.http.post<Etudiant>(`${this.baseUrl}/add-etudiant`, etudiant);
  }

  update(etudiant: Etudiant): Observable<Etudiant> {
    return this.http.put<Etudiant>(`${this.baseUrl}/modify-etudiant`, etudiant);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/remove-etudiant/${id}`);
  }
}