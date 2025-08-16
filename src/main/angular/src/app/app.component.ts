import {Component} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {LayoutComponent} from './layout/layout/layout.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LayoutComponent],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'angular';
}
