// routes.ts
import { LayoutComponent } from './layout/layout/layout.component';
import { AppComponent } from './app.component';
import { Routes } from '@angular/router';
import {MiscComponent} from './pages/misc/misc.component';
import {BookComponent} from './pages/websocket-channels/book/components/book.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: MiscComponent },
      {
        path: 'ws/book', component: BookComponent,
      }
    ]
  }
];
