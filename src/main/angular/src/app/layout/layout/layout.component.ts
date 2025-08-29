import { Component } from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-layout',
  imports: [
    RouterLink,
    RouterOutlet
//    RouterLinkActive
  ],
  templateUrl: './layout.component.html',
  standalone: true,
  styleUrl: './layout.component.css'
})
export class LayoutComponent {

}
