import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenPriceAllTimeframesComponent } from './open-price-all-timeframes.component';

describe('OpenPriceAllTimeframesComponent', () => {
  let component: OpenPriceAllTimeframesComponent;
  let fixture: ComponentFixture<OpenPriceAllTimeframesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OpenPriceAllTimeframesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OpenPriceAllTimeframesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
