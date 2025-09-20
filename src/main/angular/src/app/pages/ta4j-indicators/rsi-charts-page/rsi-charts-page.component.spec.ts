import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RsiChartsPageComponent } from './rsi-charts-page.component';

describe('RsiChartsPageComponent', () => {
  let component: RsiChartsPageComponent;
  let fixture: ComponentFixture<RsiChartsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RsiChartsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RsiChartsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
